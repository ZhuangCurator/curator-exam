package com.curator.backend.paper.question.bank.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.backend.paper.question.bank.entity.dto.QuestionBankDTO;
import com.curator.backend.paper.question.bank.entity.vo.info.QuestionBankInfo;
import com.curator.backend.paper.question.bank.entity.vo.seacrh.QuestionBankSearch;
import com.curator.backend.paper.question.entity.dto.QuestionDTO;
import com.curator.backend.paper.question.entity.vo.search.QuestionSearch;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;

import java.util.List;

/**
 * 试题库库 服务类
 *
 * @author Jun
 * @since 2021-05-08
 */
public interface QuestionBankService {

    /**
     * 试题库分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<PageResult<QuestionBankDTO>> pageWithQuestionBank(QuestionBankSearch search);

    /**
     * 试题库列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<List<QuestionBankDTO>> listWithQuestionBank(QuestionBankSearch search);

    /**
     * 查询试题库
     *
     * @param id 试题库ID
     * @return {@link ResultResponse}
     */
    ResultResponse<QuestionBankDTO> getQuestionBank(String id);

    /**
     * 添加试题库
     *
     * @param info 试题库信息
     * @return {@link ResultResponse}
     */
    ResultResponse<QuestionBankDTO> saveQuestionBank(QuestionBankInfo info);

    /**
     * 编辑试题库
     *
     * @param info 试题库信息
     * @return {@link ResultResponse}
     */
    ResultResponse<QuestionBankDTO> putQuestionBank(QuestionBankInfo info);

    /**
     * 删除试题库
     *
     * @param id 试题库ID
     * @return {@link ResultResponse}
     */
    ResultResponse<String> removeQuestionBank(String id);

    /**
     * 试题库添加试题
     *
     * @param info 试题库信息
     * @return {@link ResultResponse}
     */
    ResultResponse<?> bindQuestionToQuestionBank(QuestionBankInfo info);

    /**
     * 试题库中试题分页查询
     *
     * @param search 试题分页查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<PageResult<QuestionDTO>> pageWithQuestion(QuestionSearch search);

    /**
     * 试题库删除试题
     *
     * @param info 试题库信息
     * @return {@link ResultResponse}
     */
    ResultResponse<?> removeQuestionFromQuestionBank(QuestionBankInfo info);
}
