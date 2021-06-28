package com.curator.backend.paper.test.paper.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.backend.paper.question.entity.Question;
import com.curator.backend.paper.question.entity.QuestionAnswer;
import com.curator.backend.paper.question.mapper.QuestionAnswerMapper;
import com.curator.backend.paper.question.mapper.QuestionMapper;
import com.curator.backend.paper.test.paper.entity.TestPaper;
import com.curator.backend.paper.test.paper.entity.TestPaperQuestion;
import com.curator.backend.paper.test.paper.entity.dto.TestPaperDTO;
import com.curator.backend.paper.test.paper.entity.dto.TestPaperQuestionDTO;
import com.curator.backend.paper.test.paper.entity.vo.search.TestPaperSearch;
import com.curator.backend.paper.test.paper.mapper.TestPaperMapper;
import com.curator.backend.paper.test.paper.mapper.TestPaperQuestionMapper;
import com.curator.backend.paper.test.paper.service.TestPaperService;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 考生试卷 服务实现类
 *
 * @author Jun
 * @since 2021-05-12
 */
@Service
public class TestPaperServiceImpl implements TestPaperService {

    @Autowired
    private TestPaperMapper testPaperMapper;
    @Autowired
    private TestPaperQuestionMapper testPaperQuestionMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionAnswerMapper questionAnswerMapper;

    @Override
    public ResultResponse<PageResult<TestPaperDTO>> pageWithTestPaper(TestPaperSearch search) {
        Page<TestPaper> page = new Page<>(search.getCurrent(), search.getPageSize());
        QueryWrapper<TestPaper> wrapper = new QueryWrapper<>();

        wrapper.eq(Help.isNotEmpty(search.getTestPaperId()), "test_paper_id", search.getTestPaperId())
                .eq(Help.isNotEmpty(search.getExamRegisterInfoId()), "exam_register_info_id", search.getExamRegisterInfoId())
                .orderByDesc("create_time");
        IPage<TestPaper> iPage = testPaperMapper.selectPage(page, wrapper);
        List<TestPaperDTO> resultList = iPage.getRecords().stream()
                .map(this::convertEntity).collect(Collectors.toList());

        PageResult<TestPaperDTO> resultPage = new PageResult<>();
        BeanUtils.copyProperties(iPage, resultPage, "records");
        resultPage.setRecords(resultList);

        return ResultResponse.<PageResult<TestPaperDTO>>builder().success("考生试卷分页查询成功").data(resultPage).build();

    }

    @Override
    public ResultResponse<List<TestPaperQuestionDTO>> listQuestionWithTestPaper(String testPaperId) {

        QueryWrapper<TestPaperQuestion> wrapper = new QueryWrapper<>();
        wrapper.eq("test_paper_id", testPaperId)
                .orderByAsc("question_sort");
        List<TestPaperQuestion> list = testPaperQuestionMapper.selectList(wrapper);
        if (Help.isEmpty(list)) {
            return ResultResponse.<List<TestPaperQuestionDTO>>builder().failure("该试卷未查到所属试题,试卷初始化未成功!").build();
        }
        List<TestPaperQuestionDTO> resultList = list.parallelStream().map(this::convertEntity).collect(Collectors.toList());
        return ResultResponse.<List<TestPaperQuestionDTO>>builder().success("该试卷试题列表查询成功!").data(resultList).build();
    }

    /**
     * 将 数据库对象 转为 数据传输对象
     *
     * @param entity 数据库信息
     * @return dto实体类
     */
    private TestPaperDTO convertEntity(TestPaper entity) {
        TestPaperDTO target = new TestPaperDTO();
        if (Help.isNotEmpty(entity)) {
            BeanUtils.copyProperties(entity, target);
        }
        return target;
    }

    /**
     * 将 数据库对象 转为 数据传输对象
     *
     * @param entity 数据库信息
     * @return dto实体类
     */
    private TestPaperQuestionDTO convertEntity(TestPaperQuestion entity) {
        TestPaperQuestionDTO target = new TestPaperQuestionDTO();
        if (Help.isNotEmpty(entity)) {
            BeanUtils.copyProperties(entity, target);
            // 获取题干
            Question question = questionMapper.selectById(entity.getQuestionId());
            if(Help.isNotEmpty(question)) {
                target.setQuestionStem(question.getQuestionStem());
            }
            // 解析考生答案
            if(Help.isNotEmpty(entity.getUserAnswer())) {
                List<String> userAnswerList = Help.split2List(entity.getUserAnswer(), "\\$:\\$");
                target.setUserAnswerList(userAnswerList);
            }
            // 获取试题答案内容
            QueryWrapper<QuestionAnswer> wrapper = new QueryWrapper<>();
            wrapper.eq("question_id", entity.getQuestionId())
                    .orderByAsc("question_answer_order");
            List<QuestionAnswer> questionAnswerList = questionAnswerMapper.selectList(wrapper);
            if(Help.isNotEmpty(questionAnswerList)) {
                List<String> answerList = questionAnswerList.stream().map(QuestionAnswer::getContent).collect(Collectors.toList());
                target.setQuestionAnswerList(answerList);
            }
        }
        return target;
    }

}
