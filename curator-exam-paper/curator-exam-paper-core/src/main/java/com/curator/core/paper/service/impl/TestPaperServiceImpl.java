package com.curator.core.paper.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.curator.api.paper.enums.QuestionTypeEnum;
import com.curator.api.paper.enums.TestPaperStatusEnum;
import com.curator.api.paper.pojo.dto.GenerationRuleDetailDTO;
import com.curator.api.paper.pojo.dto.PaperQuestionDTO;
import com.curator.api.paper.pojo.vo.TestPaperInfo;
import com.curator.api.register.pojo.dto.ExamRegisterInfoDTO;
import com.curator.api.register.provider.ExamRegisterInfoProvider;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.common.util.RedissonUtil;
import com.curator.core.paper.entity.*;
import com.curator.core.paper.mapper.*;
import com.curator.core.paper.service.TestPaperService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 考生试卷 服务实现类
 *
 * @author Jun
 * @since 2021-05-12
 */
@Service
public class TestPaperServiceImpl implements TestPaperService {

    private final Snowflake snowflake = new Snowflake(1, 2);
    @Autowired
    private TestPaperMapper testPaperMapper;
    @Autowired
    private TestPaperQuestionMapper testPaperQuestionMapper;
    @Autowired
    private PaperGenerationRuleMapper generationRuleMapper;
    @Autowired
    private PaperGenerationRuleDetailMapper generationRuleDetailMapper;
    @Autowired
    private BankQuestionMapper bankQuestionMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionAnswerMapper questionAnswerMapper;
    @DubboReference
    private ExamRegisterInfoProvider registerInfoProvider;

    @Override
    public ResultResponse<ExamRegisterInfoDTO> accountLogin(String accountName, String admissionNumber) {
        return registerInfoProvider.accountLogin(accountName, admissionNumber);
    }

    @Override
    public ResultResponse<ExamRegisterInfoDTO> verifyPassword(TestPaperInfo info) {
        ResultResponse<ExamRegisterInfoDTO> res = registerInfoProvider.checkExamPassword(info.getExamRegisterInfoId(), info.getExamPassword());
        if(res.getSucceeded()) {
            ExamRegisterInfoDTO dto = res.getData();
            // 检查成功之后，更新试卷的开始时间和结束时间
            TestPaper entity = new TestPaper();
            entity.setTestPaperId(info.getTestPaperId());
            entity.setPaperStartTime(dto.getExamStartTime());
            entity.setPaperEndTime(dto.getExamEndTime());
            testPaperMapper.updateById(entity);
        }
        return res;
    }

    @Override
    public ResultResponse<String> initTestPaper(TestPaperInfo info) {
        // 首先判断该考生是否已考过试了
        QueryWrapper<TestPaper> paperWrapper = new QueryWrapper<>();
        paperWrapper.eq("exam_register_info_id", info.getExamRegisterInfoId())
                .eq("paper_status", TestPaperStatusEnum.OVER.getStatus());
        Integer count = testPaperMapper.selectCount(paperWrapper);
        if (count != null && count > 0) {
            return ResultResponse.<String>builder().failure("您已完成此次考试!").build();
        }
        // 接着判断该用户是否拥有未开考或考试进行中的试卷,若有,则返回
        paperWrapper = new QueryWrapper<>();
        paperWrapper.eq("exam_register_info_id", info.getExamRegisterInfoId())
                .and(w -> w.eq("paper_status", TestPaperStatusEnum.UN_STARTED.getStatus())
                        .or()
                        .eq("paper_status", TestPaperStatusEnum.PROCESSING.getStatus())
                );
        TestPaper testPaper = testPaperMapper.selectOne(paperWrapper);
        if (Help.isNotEmpty(testPaper)) {
            return ResultResponse.<String>builder().success("您含有未开考或考试进行中的试卷,请继续考试!").data(testPaper.getTestPaperId()).build();
        }
        // 接着判断该用户是否拥有 考试需原卷重考 的试卷,若有,复制一份试卷作为新试卷返回
        paperWrapper = new QueryWrapper<>();
        paperWrapper.eq("exam_register_info_id", info.getExamRegisterInfoId())
                .eq("paper_status", TestPaperStatusEnum.OLD_PAPER_RETAKE.getStatus());
        testPaper = testPaperMapper.selectOne(paperWrapper);
        if (Help.isNotEmpty(testPaper)) {
            String testPaperId = generateTestPaperByOldPaper(testPaper);
            return ResultResponse.<String>builder().success("试卷初始化完成,请准备开始考试!").data(testPaperId).build();
        }
        // 接着判断该用户是否拥有 考试需新卷重考 的试卷,若有,初始化一份新试卷返回
        paperWrapper = new QueryWrapper<>();
        paperWrapper.eq("exam_register_info_id", info.getExamRegisterInfoId())
                .eq("paper_status", TestPaperStatusEnum.NEW_PAPER_RETAKE.getStatus());
        testPaper = testPaperMapper.selectOne(paperWrapper);
        if (Help.isNotEmpty(testPaper)) {
            // 废弃旧的试卷
            testPaper.setPaperStatus(TestPaperStatusEnum.DISCARD.getStatus());
            testPaperMapper.update(testPaper, new UpdateWrapper<TestPaper>().eq("test_paper_id", testPaper.getTestPaperId()));
            // 生成新试卷
            return generateTestPaper(info.getExamRegisterInfoId(), info.getGenerationRuleId());
        }
        // 若都没有上述试卷,生成新试卷即可
        return generateTestPaper(info.getExamRegisterInfoId(), info.getGenerationRuleId());
    }

    @Override
    public ResultResponse<List<GenerationRuleDetailDTO>> getQuestionTypeAndNum(String generationRuleId) {
        QueryWrapper<PaperGenerationRuleDetail> wrapper = new QueryWrapper<>();
        wrapper.eq("generation_rule_id", generationRuleId);
        List<PaperGenerationRuleDetail> ruleDetailList = generationRuleDetailMapper.selectList(wrapper);
        Comparator.comparing(PaperGenerationRuleDetail::getDetailSort);
        List<GenerationRuleDetailDTO> filterList = ruleDetailList.stream()
                .sorted(Comparator.comparing(PaperGenerationRuleDetail::getDetailSort))
                .map(entity -> {
                    GenerationRuleDetailDTO dto = new GenerationRuleDetailDTO();
                    BeanUtils.copyProperties(entity, dto);
                    return dto;
                }).collect(Collectors.toList());
        return ResultResponse.<List<GenerationRuleDetailDTO>>builder().success("试卷试题类型及数量查询成功").data(filterList).build();
    }

    @Override
    public ResultResponse<PaperQuestionDTO> getPaperQuestion(TestPaperInfo info) {
        // 首先判断试卷状态
        TestPaper testPaper = testPaperMapper.selectById(info.getTestPaperId());
        if(testPaper.getPaperStatus() == TestPaperStatusEnum.NEW_PAPER_RETAKE.getStatus()
            || testPaper.getPaperStatus() == TestPaperStatusEnum.OLD_PAPER_RETAKE.getStatus()
            || testPaper.getPaperStatus() == TestPaperStatusEnum.DISCARD.getStatus()) {
            return ResultResponse.<PaperQuestionDTO>builder().failure("该试卷已废弃,请重新登录进行考试!").build();
        } else if(testPaper.getPaperStatus() == TestPaperStatusEnum.UN_STARTED.getStatus()) {
            return ResultResponse.<PaperQuestionDTO>builder().failure("请在考试口令验证成功之后再进行考试！").build();
        }
        // 首先查询缓存
        String redisKey = generateRedisKeyWithQuestion(info.getExamRegisterInfoId(), info.getTestPaperId());
        PaperQuestionDTO paperQuestionDTO = RedissonUtil.getCacheMap(redisKey, String.valueOf(info.getPaperQuestionSort()));
        if(Help.isNotEmpty(paperQuestionDTO)) {
            return ResultResponse.<PaperQuestionDTO>builder().success("试题查询成功").data(paperQuestionDTO).build();
        }
        // 缓存不存在则查询数据库
        QueryWrapper<TestPaperQuestion> wrapper = new QueryWrapper<>();
        wrapper.eq("exam_register_info_id", info.getExamRegisterInfoId())
                .eq("test_paper_id", info.getTestPaperId())
                .eq("question_sort", info.getPaperQuestionSort());
        TestPaperQuestion paperQuestion = testPaperQuestionMapper.selectOne(wrapper);
        if(Help.isEmpty(paperQuestion)) {
            return ResultResponse.<PaperQuestionDTO>builder().failure("该试题不存在！").data(paperQuestionDTO).build();
        }
        PaperQuestionDTO dto = cachePaperQuestion(paperQuestion);
        return ResultResponse.<PaperQuestionDTO>builder().success("试题查询成功").data(dto).build();
    }

    @Override
    public ResultResponse<String> saveUserAnswer(TestPaperInfo info) {
        // 说明，多个答案间使用$:$隔开，填空题每个空格必须有答案，考生没填那么答案默认为字符 null
        // 首先判断试卷状态
        TestPaper testPaper = testPaperMapper.selectById(info.getTestPaperId());
        if(testPaper.getPaperStatus() == TestPaperStatusEnum.NEW_PAPER_RETAKE.getStatus()
                || testPaper.getPaperStatus() == TestPaperStatusEnum.OLD_PAPER_RETAKE.getStatus()
                || testPaper.getPaperStatus() == TestPaperStatusEnum.DISCARD.getStatus()) {
            return ResultResponse.<String>builder().failure("该试卷已废弃,请重新登录进行考试!").build();
        }
        QueryWrapper<TestPaperQuestion> wrapper = new QueryWrapper<>();
        wrapper.eq("exam_register_info_id", info.getExamRegisterInfoId())
                .eq("test_paper_id", info.getTestPaperId())
                .eq("question_sort", info.getPaperQuestionSort());
        TestPaperQuestion paperQuestion = testPaperQuestionMapper.selectOne(wrapper);
        if(Help.isEmpty(paperQuestion)) {
            return ResultResponse.<String>builder().failure("该试题不存在!").build();
        }
        // 保存答案
        paperQuestion.setHandled(1);
        paperQuestion.setUserAnswer(info.getUserAnswer());
        // 计算此题所获得的分数
        if(Help.isEmpty(info.getUserAnswer())) {
            paperQuestion.setUserPoint(new BigDecimal(0));
        } else {
            paperQuestion.setUserPoint(calculateQuestionPoint(paperQuestion));
        }
        testPaperQuestionMapper.update(paperQuestion, new UpdateWrapper<TestPaperQuestion>().eq("test_paper_question_id", paperQuestion.getTestPaperQuestionId()));
        // 缓存试题
        cachePaperQuestion(paperQuestion);
        return ResultResponse.<String>builder().success("用户答案保存成功!").build();
    }

    @Override
    public ResultResponse<String> markTestPaper(TestPaperInfo info) {
        QueryWrapper<TestPaperQuestion> wrapper = new QueryWrapper<>();
        wrapper.eq("exam_register_info_id", info.getExamRegisterInfoId())
                .eq("test_paper_id", info.getTestPaperId())
                .eq("is_handled", 1);
        List<TestPaperQuestion> paperQuestionList = testPaperQuestionMapper.selectList(wrapper);
        double score = paperQuestionList.stream().mapToDouble(paperQuestion -> paperQuestion.getUserPoint().doubleValue()).sum();
        // 更新试卷分数
        TestPaper testPaper = new TestPaper();
        testPaper.setTestPaperId(info.getTestPaperId());
        testPaper.setExamPoint(new BigDecimal(score));
        testPaper.setHandInReason(info.getHandInReason());
        testPaper.setHandInTime(LocalDateTime.now());
        testPaper.setPaperStatus(TestPaperStatusEnum.OVER.getStatus());
        testPaperMapper.updateById(testPaper);
        // 同步分数到报名信息中
        registerInfoProvider.synchronizeScore(info.getExamRegisterInfoId(), testPaper.getExamPoint());
        return ResultResponse.<String>builder().success("阅卷成功!").data(String.valueOf(score)).build();
    }

    /**
     * 初始化新试卷
     *
     * @param examRegisterInfoId       考生信息ID
     * @param generationRuleId 试卷生成规则ID
     * @return
     */
    private ResultResponse<String> generateTestPaper(String examRegisterInfoId, String generationRuleId) {

        PaperGenerationRule generationRule = generationRuleMapper.selectById(generationRuleId);
        // 插入试卷试题
        QueryWrapper<PaperGenerationRuleDetail> wrapper = new QueryWrapper<>();
        wrapper.eq("generation_rule_id", generationRuleId);
        List<PaperGenerationRuleDetail> ruleDetailList = generationRuleDetailMapper.selectList(wrapper);
        if (Help.isEmpty(ruleDetailList)) {
            return ResultResponse.<String>builder().failure("该试卷生成规则没有设置规则详情,无法初始化试卷!").build();
        }
        // 插入新试卷
        TestPaper entity = new TestPaper();
        entity.setExamRegisterInfoId(examRegisterInfoId);
        entity.setTotalPoint(generationRule.getTestPaperPoint());
        entity.setPaperStatus(TestPaperStatusEnum.UN_STARTED.getStatus());
        testPaperMapper.insert(entity);
        // 按组卷顺序抽题
        List<TestPaperQuestion> paperQuestionList = new ArrayList<>();
        AtomicInteger serialNum = new AtomicInteger(0);
        ruleDetailList.stream().sorted(Comparator.comparing(PaperGenerationRuleDetail::getDetailSort)).forEach(rule -> {
            // 首先查看试题库中满足此规则的试题数量
            QueryWrapper<BankQuestion> bankQuestionWrapper = new QueryWrapper<>();
            bankQuestionWrapper.eq("question_type", rule.getQuestionType())
                    .eq("question_point", rule.getQuestionPoint())
                    .eq("question_difficulty", rule.getQuestionDifficulty())
                    .eq("question_bank_id", generationRule.getQuestionBankId());
            List<BankQuestion> bankQuestionList = bankQuestionMapper.selectList(bankQuestionWrapper);
            int size = bankQuestionList.size();
            if (size > rule.getQuestionNumber()) {
                for (int i = size - 1; i >= 1; i--) {
                    int randomPos = new Random().nextInt(i);
                    Collections.swap(bankQuestionList, i, randomPos);
                }
                bankQuestionList = bankQuestionList.subList(0, rule.getQuestionNumber());
            }
            List<TestPaperQuestion> list = bankQuestionList.parallelStream().map(bankQuestion -> {
                TestPaperQuestion paperQuestion = new TestPaperQuestion();
                paperQuestion.setTestPaperQuestionId(snowflake.nextIdStr());
                paperQuestion.setExamRegisterInfoId(examRegisterInfoId);
                paperQuestion.setTestPaperId(entity.getTestPaperId());
                paperQuestion.setQuestionId(bankQuestion.getQuestionId());
                paperQuestion.setQuestionType(bankQuestion.getQuestionType());
                paperQuestion.setQuestionSort(serialNum.incrementAndGet());
                paperQuestion.setDeleted(0);
                paperQuestion.setCreateTime(LocalDateTime.now());
                paperQuestion.setUpdateTime(LocalDateTime.now());
                // 缓存试题
                cachePaperQuestion(paperQuestion);
                return paperQuestion;
            }).collect(Collectors.toList());
            paperQuestionList.addAll(list);
        });
        testPaperQuestionMapper.batchAddPaperQuestion(paperQuestionList);
        return ResultResponse.<String>builder().success("试卷初始化完成,请准备开始考试!").data(entity.getTestPaperId()).build();
    }

    /**
     * 根据旧的试卷生成新的试卷
     *
     * @param oldPaper 旧试卷
     * @return
     */
    private String generateTestPaperByOldPaper(TestPaper oldPaper) {
        // 废弃旧的试卷
        oldPaper.setPaperStatus(TestPaperStatusEnum.DISCARD.getStatus());
        testPaperMapper.update(oldPaper, new UpdateWrapper<TestPaper>().eq("test_paper_id", oldPaper.getTestPaperId()));
        // 插入新试卷
        TestPaper entity = new TestPaper();
        entity.setExamRegisterInfoId(oldPaper.getExamRegisterInfoId());
        entity.setTotalPoint(oldPaper.getTotalPoint());
        entity.setPaperStatus(TestPaperStatusEnum.UN_STARTED.getStatus());
        testPaperMapper.insert(entity);
        // 复制旧试卷试题至新试卷
        QueryWrapper<TestPaperQuestion> wrapper = new QueryWrapper<>();
        wrapper.eq("test_paper_id", oldPaper.getTestPaperId());
        List<TestPaperQuestion> questionList = testPaperQuestionMapper.selectList(wrapper);
        List<TestPaperQuestion> newQuestionList = questionList.parallelStream().map(question -> {
            TestPaperQuestion paperQuestion = new TestPaperQuestion();
            paperQuestion.setTestPaperQuestionId(snowflake.nextIdStr());
            paperQuestion.setQuestionId(question.getQuestionId());
            paperQuestion.setTestPaperId(entity.getTestPaperId());
            paperQuestion.setExamRegisterInfoId(question.getExamRegisterInfoId());
            paperQuestion.setQuestionSort(question.getQuestionSort());
            paperQuestion.setQuestionType(question.getQuestionType());
            paperQuestion.setDeleted(0);
            paperQuestion.setCreateTime(LocalDateTime.now());
            paperQuestion.setUpdateTime(LocalDateTime.now());
            // 缓存试题
            cachePaperQuestion(paperQuestion);
            return paperQuestion;
        }).collect(Collectors.toList());
        testPaperQuestionMapper.batchAddPaperQuestion(newQuestionList);
        return entity.getTestPaperId();
    }

    /**
     * 计算该试题考生所获分数
     *
     * @param paperQuestion 试卷试题
     * @return
     */
    private BigDecimal calculateQuestionPoint(TestPaperQuestion paperQuestion){
        Question question = questionMapper.selectById(paperQuestion.getQuestionId());
        QueryWrapper<QuestionAnswer> wrapper = new QueryWrapper<>();
        wrapper.eq("question_id", paperQuestion.getQuestionId())
                .orderByAsc("question_answer_order");
        // 试题答案集合
        List<QuestionAnswer> questionAnswerList = questionAnswerMapper.selectList(wrapper);
        // 考生答案集合
        List<String> userAnswerList = Help.split2List(paperQuestion.getUserAnswer(), "\\$:\\$");
        BigDecimal point = new BigDecimal(0);
        if(paperQuestion.getQuestionType() == QuestionTypeEnum.FILL_BLANK.getStatus()) {
            // 填空题直接比对答案(答对一空即有分)
            if(question.getOrdered() == 1) {
                // 答案有序
                for (int i = 0; i < userAnswerList.size(); i++) {
                    String userAnswer = userAnswerList.get(i);
                    if(Help.isNotEmpty(userAnswer)) {
                        if(userAnswer.equals(questionAnswerList.get(i).getContent())) {
                            point = point.add(questionAnswerList.get(i).getQuestionPoint());
                        }
                    }
                }
            } else {
                // 答案无序
                List<String> questionAnswerStrList = questionAnswerList.stream().map(QuestionAnswer::getContent).collect(Collectors.toList());
                for (int i = 0; i < userAnswerList.size(); i++) {
                    String userAnswer = userAnswerList.get(i);
                    if(Help.isNotEmpty(userAnswer)) {
                        if(questionAnswerStrList.contains(userAnswer)){
                            point = point.add(questionAnswerList.get(i).getQuestionPoint());
                        }
                    }
                }
            }
        }else {
            // 单选题、多选题、判断题 比对序号
            for (QuestionAnswer questionAnswer : questionAnswerList) {
                String answerStr = (char) ((questionAnswer.getQuestionAnswerOrder() - 1) % 26 + (int) 'A') + "";
                if(userAnswerList.contains(answerStr)){
                    if(questionAnswer.getRighted() == 1) {
                        point = point.add(questionAnswer.getQuestionPoint());
                    }else {
                        // 选中了错误答案即一分都没有
                        point = new BigDecimal(0);
                        break;
                    }
                }
            }
        }

        return point;
    }

    /**
     * 缓存试题
     * @param question 数据库试题对象
     */
    private PaperQuestionDTO cachePaperQuestion(TestPaperQuestion question) {
        PaperQuestionDTO paperQuestionDTO = new PaperQuestionDTO();
        BeanUtils.copyProperties(question, paperQuestionDTO);
        Question questionEntity = questionMapper.selectById(question.getQuestionId());
        paperQuestionDTO.setQuestionStem(questionEntity.getQuestionStem());
        if (paperQuestionDTO.getQuestionType() != QuestionTypeEnum.FILL_BLANK.getStatus()) {
            QueryWrapper<QuestionAnswer> wrapper = new QueryWrapper<>();
            wrapper.eq("question_id", paperQuestionDTO.getQuestionId())
                    .orderByAsc("question_answer_order");
            List<QuestionAnswer> questionAnswerList = questionAnswerMapper.selectList(wrapper);
            List<PaperQuestionDTO.PaperQuestionAnswer> list = questionAnswerList.stream().map(questionAnswer -> {
                PaperQuestionDTO.PaperQuestionAnswer answerDTO = new PaperQuestionDTO.PaperQuestionAnswer();
                answerDTO.setQuestionAnswerContent(questionAnswer.getContent());
                return answerDTO;
            }).collect(Collectors.toList());
            paperQuestionDTO.setQuestionAnswerList(list);
        }
        if(Help.isNotEmpty(question.getUserAnswer())) {
            List<String> userAnswerList = Help.split2List(question.getUserAnswer(), "\\$:\\$");
            paperQuestionDTO.setUserAnswerList(userAnswerList);
        }
        String redisKey = generateRedisKeyWithQuestion(paperQuestionDTO.getExamRegisterInfoId(), paperQuestionDTO.getTestPaperId());
        RedissonUtil.setCacheMap(redisKey, new HashMap<String, PaperQuestionDTO>(8){{put(String.valueOf(paperQuestionDTO.getQuestionSort()), paperQuestionDTO);}});
        RedissonUtil.expire(redisKey, 60, TimeUnit.MINUTES);
        return paperQuestionDTO;
    }

    /**
     * 构建试题缓存 键
     *
     * @param examRegisterInfoId 考生信息ID
     * @param testPaperId 试卷ID
     * @return
     */
    private String generateRedisKeyWithQuestion(String examRegisterInfoId, String testPaperId) {
        return "EXAMINFOID:TESTPAPERID:" + examRegisterInfoId + ":" + testPaperId;
    }
}
