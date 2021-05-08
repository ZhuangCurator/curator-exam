package com.curator.backend.paper.question.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import com.curator.common.util.ServletUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        wrapper.eq(Help.isNotEmpty(search.getQuestionType()), "question_type", search.getQuestionType())
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
        if (Help.isEmpty(info.getQuestionAnswerInfoList())) {
            return ResultResponse.<QuestionDTO>builder().failure("试题答案不能为空").build();
        }
        Question entity = convertInfo(info);
        entity.setCreateAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID));
        entity.setParentAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_PARENT_ID));
        questionMapper.insert(entity);
        // 保存答案
        List<QuestionAnswerInfo> questionAnswerInfoList = info.getQuestionAnswerInfoList();
        // 正确答案个数
        long size = questionAnswerInfoList.stream().filter(questionAnswerInfo -> questionAnswerInfo.getRight() == 1).count();
        // 四舍五入,保留两位小数
        BigDecimal answerPoint = new BigDecimal(entity.getQuestionPoint()).divide(new BigDecimal(size), 2, BigDecimal.ROUND_HALF_UP);
        questionAnswerInfoList.forEach(questionAnswerInfo -> {
            QuestionAnswer answerEntity = convertInfo(questionAnswerInfo);
            answerEntity.setQuestionId(entity.getQuestionId());
            answerEntity.setQuestionPoint(answerPoint);
            answerEntity.setCreateAccountId(entity.getCreateAccountId());
            answerEntity.setParentAccountId(entity.getParentAccountId());
            questionAnswerMapper.insert(answerEntity);
        });
        return ResultResponse.<QuestionDTO>builder().success("试题添加成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<QuestionDTO> putQuestion(QuestionInfo info) {
        Question entity = convertInfo(info);
        questionMapper.update(entity, new UpdateWrapper<Question>().eq("question_id", info.getQuestionId()));
        List<QuestionAnswerInfo> questionAnswerInfoList = info.getQuestionAnswerInfoList();
        // 正确答案个数
        long size = questionAnswerInfoList.stream().filter(questionAnswerInfo -> questionAnswerInfo.getRight() == 1).count();
        // 四舍五入,保留两位小数
        BigDecimal answerPoint = new BigDecimal(entity.getQuestionPoint()).divide(new BigDecimal(size), 2, BigDecimal.ROUND_HALF_UP);
        questionAnswerInfoList.forEach(questionAnswerInfo -> {
            QuestionAnswer answerEntity = convertInfo(questionAnswerInfo);
            answerEntity.setQuestionPoint(answerPoint);
            questionAnswerMapper.update(answerEntity, new UpdateWrapper<QuestionAnswer>().eq("question_answer_id", answerEntity.getQuestionAnswerId()));
        });
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
