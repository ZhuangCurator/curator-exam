package com.curator.core.paper.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.curator.api.paper.enums.QuestionTypeEnum;
import com.curator.api.paper.enums.TestPaperStatusEnum;
import com.curator.api.paper.pojo.dto.PaperQuestionDTO;
import com.curator.api.paper.pojo.vo.TestPaperInfo;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.common.util.RedissonUtil;
import com.curator.core.paper.entity.*;
import com.curator.core.paper.mapper.*;
import com.curator.core.paper.service.TestPaperService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private QuestionAnswerMapper questionAnswerMapper;

    @Override
    public ResultResponse<String> initTestPaper(TestPaperInfo info) {
        // 首先判断该考生是否已考过试了
        QueryWrapper<TestPaper> paperWrapper = new QueryWrapper<>();
        paperWrapper.eq("exam_info_id", info.getExamInfoId())
                .eq("paper_status", TestPaperStatusEnum.OVER.getStatus());
        Integer count = testPaperMapper.selectCount(paperWrapper);
        if (count != null && count > 0) {
            return ResultResponse.<String>builder().failure("您已完成此次考试!").build();
        }
        // 接着判断该用户是否拥有未开考或考试进行中的试卷,若有,则返回
        paperWrapper = new QueryWrapper<>();
        paperWrapper.eq("exam_info_id", info.getExamInfoId())
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
        paperWrapper.eq("exam_info_id", info.getExamInfoId())
                .eq("paper_status", TestPaperStatusEnum.OLD_PAPER_RETAKE.getStatus());
        testPaper = testPaperMapper.selectOne(paperWrapper);
        if (Help.isNotEmpty(testPaper)) {
            String testPaperId = generateTestPaperByOldPaper(testPaper);
            return ResultResponse.<String>builder().success("试卷初始化完成,请准备开始考试!").data(testPaperId).build();
        }
        // 接着判断该用户是否拥有 考试需新卷重考 的试卷,若有,初始化一份新试卷返回
        paperWrapper = new QueryWrapper<>();
        paperWrapper.eq("exam_info_id", info.getExamInfoId())
                .eq("paper_status", TestPaperStatusEnum.NEW_PAPER_RETAKE.getStatus());
        testPaper = testPaperMapper.selectOne(paperWrapper);
        if (Help.isNotEmpty(testPaper)) {
            // 废弃旧的试卷
            testPaper.setPaperStatus(TestPaperStatusEnum.DISCARD.getStatus());
            testPaperMapper.update(testPaper, new UpdateWrapper<TestPaper>().eq("test_paper_id", testPaper.getTestPaperId()));
            // 生成新试卷
            return generateTestPaper(info.getExamInfoId(), info.getGenerationRuleId());
        }
        // 若都没有上述试卷,生成新试卷即可
        return generateTestPaper(info.getExamInfoId(), info.getGenerationRuleId());
    }

    @Override
    public ResultResponse<PaperQuestionDTO> getPaperQuestion(TestPaperInfo info) {
        // 首先判断试卷状态
        TestPaper testPaper = testPaperMapper.selectById(info.getTestPaperId());
        if(testPaper.getPaperStatus() == TestPaperStatusEnum.NEW_PAPER_RETAKE.getStatus()
            || testPaper.getPaperStatus() == TestPaperStatusEnum.OLD_PAPER_RETAKE.getStatus()
            || testPaper.getPaperStatus() == TestPaperStatusEnum.DISCARD.getStatus()) {
            return ResultResponse.<PaperQuestionDTO>builder().failure("该试卷已废弃,请重新登录进行考试!").build();
        }
        // 首先查询缓存
        String redisKey = generateRedisKeyWithQuestion(info.getExamInfoId(), info.getTestPaperId());
        PaperQuestionDTO paperQuestionDTO = RedissonUtil.getCacheMap(redisKey, String.valueOf(info.getPaperQuestionSort()));
        if(Help.isNotEmpty(paperQuestionDTO)) {
            return ResultResponse.<PaperQuestionDTO>builder().success("试题查询成功").data(paperQuestionDTO).build();
        }
        // 缓存不存在则查询数据库
        QueryWrapper<TestPaperQuestion> wrapper = new QueryWrapper<>();
        wrapper.eq("exam_info_id", info.getExamInfoId())
                .eq("test_paper_id", info.getTestPaperId())
                .eq("question_sort", info.getPaperQuestionSort());
        TestPaperQuestion paperQuestion = testPaperQuestionMapper.selectOne(wrapper);
        PaperQuestionDTO dto = cachePaperQuestion(paperQuestion);
        return ResultResponse.<PaperQuestionDTO>builder().success("试题查询成功").data(dto).build();
    }

    /**
     * 初始化新试卷
     *
     * @param examInfoId       考生信息ID
     * @param generationRuleId 试卷生成规则ID
     * @return
     */
    private ResultResponse<String> generateTestPaper(String examInfoId, String generationRuleId) {

        PaperGenerationRule generationRule = generationRuleMapper.selectById(generationRuleId);
        // 插入新试卷
        TestPaper entity = new TestPaper();
        entity.setExamInfoId(examInfoId);
        entity.setTotalPoint(generationRule.getTestPaperPoint());
        entity.setPaperStatus(TestPaperStatusEnum.UN_STARTED.getStatus());
        testPaperMapper.insert(entity);
        // 插入试卷试题
        QueryWrapper<PaperGenerationRuleDetail> wrapper = new QueryWrapper<>();
        wrapper.eq("generation_rule_id", generationRule);
        List<PaperGenerationRuleDetail> ruleDetailList = generationRuleDetailMapper.selectList(wrapper);
        if (Help.isEmpty(ruleDetailList)) {
            return ResultResponse.<String>builder().failure("该试卷生成规则没有设置规则详情,无法初始化试卷!").build();
        }
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
                paperQuestion.setExamInfoId(examInfoId);
                paperQuestion.setTestPaperId(entity.getTestPaperId());
                paperQuestion.setQuestionId(bankQuestion.getQuestionId());
                paperQuestion.setQuestionType(bankQuestion.getQuestionType());
                paperQuestion.setQuestionSort(serialNum.incrementAndGet());
                paperQuestion.setDeleted(0);
                paperQuestion.setCreateTime(LocalDateTime.now());
                paperQuestion.setUpdateTime(LocalDateTime.now());
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
        entity.setExamInfoId(oldPaper.getExamInfoId());
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
            paperQuestion.setExamInfoId(question.getExamInfoId());
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
     * 缓存试题
     * @param question 数据库试题对象
     */
    private PaperQuestionDTO cachePaperQuestion(TestPaperQuestion question) {
        PaperQuestionDTO paperQuestionDTO = new PaperQuestionDTO();
        BeanUtils.copyProperties(question, paperQuestionDTO);
        if (paperQuestionDTO.getQuestionType() != QuestionTypeEnum.FILL_BLANK.getStatus()) {
            QueryWrapper<QuestionAnswer> wrapper = new QueryWrapper<>();
            wrapper.eq("questionId", paperQuestionDTO.getQuestionId());
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
        String redisKey = generateRedisKeyWithQuestion(paperQuestionDTO.getExamInfoId(), paperQuestionDTO.getTestPaperId());
        RedissonUtil.setCacheMap(redisKey, new HashMap<String, PaperQuestionDTO>(8){{put(String.valueOf(paperQuestionDTO.getQuestionSort()), paperQuestionDTO);}});
        RedissonUtil.expire(redisKey, 60, TimeUnit.MINUTES);
        return paperQuestionDTO;
    }

    /**
     * 构建试题缓存 键
     *
     * @param examInfoId 考生信息ID
     * @param testPaperId 试卷ID
     * @return
     */
    private String generateRedisKeyWithQuestion(String examInfoId, String testPaperId) {
        return "EXAMINFOID:TESTPAPERID:" + examInfoId + ":" + testPaperId;
    }
}
