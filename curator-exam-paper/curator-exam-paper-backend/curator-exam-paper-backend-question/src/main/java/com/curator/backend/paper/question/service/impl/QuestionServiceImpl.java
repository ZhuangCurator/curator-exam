package com.curator.backend.paper.question.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.api.paper.enums.QuestionTypeEnum;
import com.curator.backend.paper.question.entity.Question;
import com.curator.backend.paper.question.entity.QuestionAnswer;
import com.curator.backend.paper.question.entity.dto.QuestionAnswerDTO;
import com.curator.backend.paper.question.entity.dto.QuestionDTO;
import com.curator.backend.paper.question.entity.vo.info.QuestionAnswerInfo;
import com.curator.backend.paper.question.entity.vo.info.QuestionInfo;
import com.curator.backend.paper.question.entity.vo.search.QuestionSearch;
import com.curator.backend.paper.question.mapper.QuestionAnswerMapper;
import com.curator.backend.paper.question.mapper.QuestionMapper;
import com.curator.backend.paper.question.service.QuestionService;
import com.curator.common.constant.CommonConstant;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.common.util.RedissonUtil;
import com.curator.common.util.ServletUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 试题 服务实现类
 *
 * @author Jun
 * @since 2021-05-08
 */
@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionAnswerMapper questionAnswerMapper;

    @Override
    public ResultResponse<PageResult<QuestionDTO>> pageWithQuestion(QuestionSearch search) {
        Page<Question> page = new Page<>(search.getCurrent(), search.getPageSize());
        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(w -> w.eq("parent_account_id", createAccountId)));
        }
        wrapper.like(Help.isNotEmpty(search.getQuestionStem()), "question_stem", search.getQuestionStem())
                .eq(Help.isNotEmpty(search.getQuestionType()), "question_type", search.getQuestionType())
                .eq(Help.isNotEmpty(search.getQuestionDifficulty()), "question_difficulty", search.getQuestionDifficulty())
                .in(Help.isNotEmpty(search.getQuestionIdList()), "question_id", search.getQuestionIdList())
                .orderByDesc("create_time");
        IPage<Question> iPage = questionMapper.selectPage(page, wrapper);
        List<QuestionDTO> resultList = iPage.getRecords().stream()
                .map(this::convertEntity).collect(Collectors.toList());

        PageResult<QuestionDTO> resultPage = new PageResult<>();
        BeanUtils.copyProperties(iPage, resultPage, "records");
        resultPage.setRecords(resultList);

        return ResultResponse.<PageResult<QuestionDTO>>builder().success("试题分页查询成功").data(resultPage).build();
    }

    @Override
    public ResultResponse<List<QuestionDTO>> listWithQuestion(QuestionSearch search) {
        QueryWrapper<Question> wrapper = new QueryWrapper<>();
        wrapper.eq(Help.isNotEmpty(search.getQuestionType()), "question_type", search.getQuestionType())
                .eq(Help.isNotEmpty(search.getQuestionDifficulty()), "question_difficulty", search.getQuestionDifficulty())
                .in(Help.isNotEmpty(search.getQuestionIdList()), "question_id", search.getQuestionIdList())
                .orderByDesc("create_time");
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(w -> w.eq("parent_account_id", createAccountId)));
        }
        List<Question> list = questionMapper.selectList(wrapper);
        List<QuestionDTO> resultList = list.stream().map(this::convertEntity).collect(Collectors.toList());

        return ResultResponse.<List<QuestionDTO>>builder().success("试题列表查询成功").data(resultList).build();
    }

    @Override
    public ResultResponse<QuestionDTO> getQuestion(String id) {
        Question entity = questionMapper.selectById(id);
        return ResultResponse.<QuestionDTO>builder().success("试题查询成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<QuestionDTO> saveQuestion(QuestionInfo info) {
        Question entity = convertInfo(info);
        entity.setCreateAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID));
        entity.setParentAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_PARENT_ID));
        questionMapper.insert(entity);
        saveQuestionAnswer(entity.getQuestionId(), info.getQuestionAnswerContentList(), info.getQuestionAnswerRightedList());
        return ResultResponse.<QuestionDTO>builder().success("试题添加成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<QuestionDTO> putQuestion(QuestionInfo info) {
        Question entity = convertInfo(info);
        questionMapper.update(entity, new UpdateWrapper<Question>().eq("question_id", info.getQuestionId()));
        questionMapper.deleteQuestionAnswer(info.getQuestionId());
        saveQuestionAnswer(info.getQuestionId(), info.getQuestionAnswerContentList(), info.getQuestionAnswerRightedList());
        return ResultResponse.<QuestionDTO>builder().success("试题更新成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<String> removeQuestion(String id) {
        questionMapper.deleteById(id);
        // 同时删除试题答案
        QueryWrapper<QuestionAnswer> wrapper = new QueryWrapper<>();
        wrapper.eq("question_id", id);
        questionAnswerMapper.delete(wrapper);
        return ResultResponse.<String>builder().success("试题删除成功").data(id).build();
    }

    @Override
    public ResultResponse<?> batchUploadQuestion() {
        final Snowflake snowflake = new Snowflake(1,1);
        String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
        String parentAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_PARENT_ID);

        List<QuestionInfo> listOk = RedissonUtil.getCacheList("QUESTION:IMPORT:OK:" + createAccountId);
        // 试题集合
        List<Question> questionList = Collections.synchronizedList(new ArrayList<>());
        // 试题答案集合
        List<QuestionAnswer> questionAnswerList = Collections.synchronizedList(new ArrayList<>());
        listOk.parallelStream().forEach(questionInfo -> {
            Question entity = convertInfo(questionInfo);
            entity.setQuestionId(snowflake.nextIdStr());
            entity.setCreateAccountId(createAccountId);
            entity.setParentAccountId(parentAccountId);
            entity.setDeleted(0);
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(LocalDateTime.now());
            questionList.add(entity);
            // 保存答案
            List<QuestionAnswerInfo> questionAnswerInfoList = questionInfo.getQuestionAnswerInfoList();
            // 正确答案个数
            long size = questionAnswerInfoList.stream().filter(questionAnswerInfo -> questionAnswerInfo.getRighted() == 1).count();
            // 四舍五入,保留两位小数
            BigDecimal answerPoint = new BigDecimal(entity.getQuestionPoint()).divide(new BigDecimal(size), 2, BigDecimal.ROUND_HALF_UP);
            questionAnswerInfoList.forEach(questionAnswerInfo -> {
                QuestionAnswer answerEntity = convertInfo(questionAnswerInfo);
                answerEntity.setQuestionAnswerId(snowflake.nextIdStr());
                answerEntity.setQuestionId(entity.getQuestionId());
                // 答案默认0分
                answerEntity.setQuestionPoint(new BigDecimal(0));
                if(questionAnswerInfo.getRighted() == 1) {
                    // 正确答案设置分数
                    answerEntity.setQuestionPoint(answerPoint);
                }
                answerEntity.setCreateAccountId(createAccountId);
                answerEntity.setParentAccountId(parentAccountId);
                answerEntity.setDeleted(0);
                answerEntity.setCreateTime(LocalDateTime.now());
                answerEntity.setUpdateTime(LocalDateTime.now());
                questionAnswerList.add(answerEntity);
            });
        });
        questionMapper.batchAddQuestion(questionList);
        questionAnswerMapper.batchAddQuestionAnswer(questionAnswerList);
        return ResultResponse.builder().success("试题批量导入成功！").build();
    }

    /**
     * 保存试题答案
     *
     * @param questionId 试题id
     * @param questionAnswerContentList 选项内容集合
     * @param questionAnswerRightedList 正确答案集合
     */
    private void saveQuestionAnswer(String questionId, List<String> questionAnswerContentList, List<String> questionAnswerRightedList) {
        Question entity = questionMapper.selectById(questionId);
        // 保存答案
        if(entity.getQuestionType() == QuestionTypeEnum.SINGLE_CHOICE.getStatus() || entity.getQuestionType() == QuestionTypeEnum.MULTIPLE_CHOICE.getStatus()) {
            // 单选题或多选题
            for (int i = 0; i < questionAnswerContentList.size(); i++) {
                QuestionAnswer answerEntity = new QuestionAnswer();
                answerEntity.setContent(questionAnswerContentList.get(i));
                answerEntity.setQuestionAnswerOrder(i + 1);
                answerEntity.setQuestionId(questionId);
                // 将序号转为大写字母
                String answer = String.valueOf((char) (i + 65));
                if(questionAnswerRightedList.contains(answer)) {
                    // 正确答案设置分数
                    answerEntity.setRighted(1);
                    // 四舍五入,保留两位小数
                    BigDecimal answerPoint = new BigDecimal(entity.getQuestionPoint()).divide(new BigDecimal(questionAnswerRightedList.size()), 2, BigDecimal.ROUND_HALF_UP);
                    answerEntity.setQuestionPoint(answerPoint);
                }
                answerEntity.setCreateAccountId(entity.getCreateAccountId());
                answerEntity.setParentAccountId(entity.getParentAccountId());
                questionAnswerMapper.insert(answerEntity);
            }
        } else if(entity.getQuestionType() == QuestionTypeEnum.JUDGMENT.getStatus()) {
            // 判断题
            int answer = Integer.parseInt(questionAnswerRightedList.get(0));
            for (int i = 0; i < 2; i++) {
                QuestionAnswer answerEntity = new QuestionAnswer();
                answerEntity.setContent(i == 1 ? "正确" : "错误");
                answerEntity.setQuestionAnswerOrder(i + 1);
                answerEntity.setQuestionId(questionId);
                if(answer == i) {
                    // 正确答案设置分数
                    answerEntity.setRighted(1);
                    answerEntity.setQuestionPoint(new BigDecimal(entity.getQuestionPoint()));
                }
                answerEntity.setCreateAccountId(entity.getCreateAccountId());
                answerEntity.setParentAccountId(entity.getParentAccountId());
                questionAnswerMapper.insert(answerEntity);
            }
        } else if(entity.getQuestionType() == QuestionTypeEnum.FILL_BLANK.getStatus()) {
            BigDecimal answerPoint = new BigDecimal(entity.getQuestionPoint()).divide(new BigDecimal(questionAnswerRightedList.size()), 2, BigDecimal.ROUND_HALF_UP);
            for (int i = 0; i < questionAnswerRightedList.size(); i++) {
                QuestionAnswer answerEntity = new QuestionAnswer();
                answerEntity.setContent(questionAnswerRightedList.get(i));
                answerEntity.setQuestionAnswerOrder(i + 1);
                answerEntity.setQuestionId(questionId);
                answerEntity.setRighted(1);
                answerEntity.setQuestionPoint(answerPoint);
                answerEntity.setCreateAccountId(entity.getCreateAccountId());
                answerEntity.setParentAccountId(entity.getParentAccountId());
                questionAnswerMapper.insert(answerEntity);
            }
        }
    }

    /**
     * 将 数据库对象 转为 数据传输对象
     *
     * @param entity 数据库信息
     * @return dto实体类
     */
    private QuestionDTO convertEntity(Question entity) {
        QuestionDTO target = new QuestionDTO();
        if (Help.isNotEmpty(entity)) {
            BeanUtils.copyProperties(entity, target);
            QueryWrapper<QuestionAnswer> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("question_id", entity.getQuestionId());
            List<QuestionAnswer> list = questionAnswerMapper.selectList(queryWrapper);
            if(entity.getQuestionType() == QuestionTypeEnum.SINGLE_CHOICE.getStatus() || entity.getQuestionType() == QuestionTypeEnum.MULTIPLE_CHOICE.getStatus()) {
                List<String> contentList = new ArrayList<>();
                List<String> answerList = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    contentList.add(list.get(i).getContent());
                    if(list.get(i).getRighted() == 1) {
                        String answer = String.valueOf((char) (i + 65));
                        answerList.add(answer);
                    }
                }
                target.setQuestionAnswerContentList(contentList);
                target.setQuestionAnswerRightedList(answerList);
            } else if (entity.getQuestionType() == QuestionTypeEnum.JUDGMENT.getStatus()) {
                QuestionAnswer right = list.stream().filter(questionAnswer -> questionAnswer.getRighted() == 1).findFirst().get();
                String answer = "正确".equals(right.getContent()) ? "1" : "0";
                target.setQuestionAnswerRightedList(Collections.singletonList(answer));
            } else if(entity.getQuestionType() == QuestionTypeEnum.FILL_BLANK.getStatus()) {
                target.setQuestionAnswerRightedList(list.stream().map(QuestionAnswer::getContent).collect(Collectors.toList()));
            }
//            if(Help.isNotEmpty(list)) {
//                List<QuestionAnswerDTO> resultList = list.parallelStream().map(this::convertEntity).collect(Collectors.toList());
//                target.setQuestionAnswerDTOList(resultList);
//            }
        }
        return target;
    }

    /**
     * 将 页面信息 转为 数据库对象
     *
     * @param info 页面信息
     * @return 数据库对应实体类
     */
    private Question convertInfo(QuestionInfo info) {
        Question target = new Question();
        if (Help.isNotEmpty(info)) {
            BeanUtils.copyProperties(info, target);
        }
        return target;
    }

    /**
     * 将 数据库对象 转为 数据传输对象
     *
     * @param entity 数据库信息
     * @return dto实体类
     */
    private QuestionAnswerDTO convertEntity(QuestionAnswer entity) {
        QuestionAnswerDTO target = new QuestionAnswerDTO();
        if (Help.isNotEmpty(entity)) {
            BeanUtils.copyProperties(entity, target);
        }
        return target;
    }

    /**
     * 将 页面信息 转为 数据库对象
     *
     * @param info 页面信息
     * @return 数据库对应实体类
     */
    private QuestionAnswer convertInfo(QuestionAnswerInfo info) {
        QuestionAnswer target = new QuestionAnswer();
        if (Help.isNotEmpty(info)) {
            BeanUtils.copyProperties(info, target);
        }
        return target;
    }
}
