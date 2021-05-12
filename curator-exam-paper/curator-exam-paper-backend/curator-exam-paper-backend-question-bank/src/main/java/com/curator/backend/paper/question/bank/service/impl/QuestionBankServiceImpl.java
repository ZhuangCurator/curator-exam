package com.curator.backend.paper.question.bank.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.backend.paper.question.bank.entity.BankQuestion;
import com.curator.backend.paper.question.bank.entity.QuestionBank;
import com.curator.backend.paper.question.bank.entity.dto.QuestionBankDTO;
import com.curator.backend.paper.question.bank.entity.vo.info.QuestionBankInfo;
import com.curator.backend.paper.question.bank.entity.vo.seacrh.QuestionBankSearch;
import com.curator.backend.paper.question.bank.mapper.BankQuestionMapper;
import com.curator.backend.paper.question.bank.mapper.QuestionBankMapper;
import com.curator.backend.paper.question.bank.service.QuestionBankService;
import com.curator.backend.paper.question.entity.Question;
import com.curator.backend.paper.question.entity.dto.QuestionDTO;
import com.curator.backend.paper.question.entity.vo.search.QuestionSearch;
import com.curator.backend.paper.question.service.QuestionService;
import com.curator.common.constant.CommonConstant;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.common.util.ServletUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 试题库库 服务实现类
 *
 * @author Jun
 * @since 2021-05-08
 */
@Service
public class QuestionBankServiceImpl implements QuestionBankService {

    @Autowired
    private QuestionBankMapper questionBankMapper;
    @Autowired
    private BankQuestionMapper bankQuestionMapper;
    @Autowired
    private QuestionService questionService;

    @Override
    public ResultResponse<PageResult<QuestionBankDTO>> pageWithQuestionBank(QuestionBankSearch search) {
        Page<QuestionBank> page = new Page<>(search.getCurrent(), search.getPageSize());
        QueryWrapper<QuestionBank> wrapper = new QueryWrapper<>();
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(w -> w.eq("parent_account_id", createAccountId)));
        }
        wrapper.like(Help.isNotEmpty(search.getQuestionBankName()), "question_bank_name", search.getQuestionBankName())
                .orderByDesc("create_time");
        IPage<QuestionBank> iPage = questionBankMapper.selectPage(page, wrapper);
        List<QuestionBankDTO> resultList = iPage.getRecords().stream()
                .map(this::convertEntity).collect(Collectors.toList());

        PageResult<QuestionBankDTO> resultPage = new PageResult<>();
        BeanUtils.copyProperties(iPage, resultPage, "records");
        resultPage.setRecords(resultList);

        return ResultResponse.<PageResult<QuestionBankDTO>>builder().success("试题库分页查询成功").data(resultPage).build();
    }

    @Override
    public ResultResponse<List<QuestionBankDTO>> listWithQuestionBank(QuestionBankSearch search) {
        QueryWrapper<QuestionBank> wrapper = new QueryWrapper<>();
        wrapper.like(Help.isNotEmpty(search.getQuestionBankName()), "question_bank_name", search.getQuestionBankName())
                .orderByDesc("create_time");
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(w -> w.eq("parent_account_id", createAccountId)));
        }
        List<QuestionBank> list = questionBankMapper.selectList(wrapper);
        List<QuestionBankDTO> resultList = list.stream().map(this::convertEntity).collect(Collectors.toList());

        return ResultResponse.<List<QuestionBankDTO>>builder().success("试题库列表查询成功").data(resultList).build();
    }

    @Override
    public ResultResponse<QuestionBankDTO> getQuestionBank(String id) {
        QuestionBank entity = questionBankMapper.selectById(id);
        return ResultResponse.<QuestionBankDTO>builder().success("试题库查询成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<QuestionBankDTO> saveQuestionBank(QuestionBankInfo info) {
        QuestionBank entity = convertInfo(info);
        entity.setCreateAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID));
        entity.setParentAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_PARENT_ID));
        questionBankMapper.insert(entity);
        return ResultResponse.<QuestionBankDTO>builder().success("试题库添加成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<QuestionBankDTO> putQuestionBank(QuestionBankInfo info) {
        QuestionBank entity = convertInfo(info);
        questionBankMapper.update(entity, new UpdateWrapper<QuestionBank>().eq("question_bank_id", info.getQuestionBankId()));
        return ResultResponse.<QuestionBankDTO>builder().success("试题库更新成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<String> removeQuestionBank(String id) {
        questionBankMapper.deleteById(id);
        return ResultResponse.<String>builder().success("试题库删除成功").data(id).build();
    }

    @Override
    public ResultResponse<?> bindQuestionToQuestionBank(QuestionBankInfo info) {
        List<String> questionIdList = info.getQuestionIdList();
        if(Help.isEmpty(questionIdList)) {
            return ResultResponse.builder().failure("请选择好试题之后在进行添加!").build();
        }
        String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
        String parentAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_PARENT_ID);
        questionIdList.forEach(questionId -> {
            QuestionDTO questionDTO = questionService.getQuestion(questionId).getData();
            if(Help.isNotEmpty(questionDTO)) {
                BankQuestion bankQuestion = new BankQuestion();
                bankQuestion.setQuestionBankId(info.getQuestionBankId());
                bankQuestion.setQuestionId(questionId);
                bankQuestion.setQuestionType(questionDTO.getQuestionType());
                bankQuestion.setQuestionPoint(questionDTO.getQuestionPoint());
                bankQuestion.setQuestionDifficulty(questionDTO.getQuestionDifficulty());
                bankQuestion.setCreateAccountId(createAccountId);
                bankQuestion.setParentAccountId(parentAccountId);
                bankQuestionMapper.insert(bankQuestion);
            }
        });
        return ResultResponse.builder().success("试题成功添加至试题库中!").build();
    }

    @Override
    public ResultResponse<PageResult<QuestionDTO>> pageWithQuestion(QuestionSearch search) {
        QueryWrapper<BankQuestion> wrapper = new QueryWrapper<>();
        wrapper.eq("question_bank_id", search.getQuestionBankId());
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(w -> w.eq("parent_account_id", createAccountId)));
        }
        List<BankQuestion> bankQuestionList = bankQuestionMapper.selectList(wrapper);
        if(Help.isEmpty(bankQuestionList)) {
            return ResultResponse.<PageResult<QuestionDTO>>builder().success("试题库中不存在任何试题").build();
        }else{
            List<String> questionIdList = bankQuestionList.stream().map(BankQuestion::getQuestionId).collect(Collectors.toList());
            search.setQuestionIdList(questionIdList);
            return questionService.pageWithQuestion(search);
        }
    }

    @Override
    public ResultResponse<?> removeQuestionFromQuestionBank(QuestionBankInfo info) {
        List<String> questionIdList = info.getQuestionIdList();
        if(Help.isEmpty(questionIdList)) {
            return ResultResponse.builder().failure("请选择好试题之后在进行删除!").build();
        }
        UpdateWrapper<BankQuestion> wrapper = new UpdateWrapper<>();
        wrapper.eq("question_bank_id", info.getQuestionBankId())
                .in("question_id", questionIdList);
        bankQuestionMapper.delete(wrapper);
        return ResultResponse.builder().success("试题成功从试题库中删除!").build();
    }

    /**
     * 将 数据库对象 转为 数据传输对象
     *
     * @param entity 数据库信息
     * @return dto实体类
     */
    private QuestionBankDTO convertEntity(QuestionBank entity) {
        QuestionBankDTO target = new QuestionBankDTO();
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
    private QuestionBank convertInfo(QuestionBankInfo info) {
        QuestionBank target = new QuestionBank();
        if (Help.isNotEmpty(info)) {
            BeanUtils.copyProperties(info, target);
        }
        return target;
    }
}
