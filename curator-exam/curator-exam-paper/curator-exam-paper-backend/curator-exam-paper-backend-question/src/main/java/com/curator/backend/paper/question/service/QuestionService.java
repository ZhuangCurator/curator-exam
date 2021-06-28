package com.curator.backend.paper.question.service;

import com.curator.backend.paper.question.entity.dto.QuestionDTO;
import com.curator.backend.paper.question.entity.vo.info.QuestionInfo;
import com.curator.backend.paper.question.entity.vo.search.QuestionSearch;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;

import java.util.List;

/**
 * 试题 服务类
 *
 * @author Jun
 * @since 2021-05-08
 */
public interface QuestionService {

    /**
     * 试题分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<PageResult<QuestionDTO>> pageWithQuestion(QuestionSearch search);

    /**
     * 试题列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<List<QuestionDTO>> listWithQuestion(QuestionSearch search);

    /**
     * 查询试题
     *
     * @param id 试题ID
     * @return {@link ResultResponse}
     */
    ResultResponse<QuestionDTO> getQuestion(String id);

    /**
     * 添加试题
     *
     * @param info 试题信息
     * @return {@link ResultResponse}
     */
    ResultResponse<QuestionDTO> saveQuestion(QuestionInfo info);

    /**
     * 编辑试题
     *
     * @param info 试题信息
     * @return {@link ResultResponse}
     */
    ResultResponse<QuestionDTO> putQuestion(QuestionInfo info);

    /**
     * 删除试题
     *
     * @param id 试题ID
     * @return {@link ResultResponse}
     */
    ResultResponse<String> removeQuestion(String id);

    /**
     * 批量上传 试题
     * @return
     */
    ResultResponse<?> batchUploadQuestion();

}
