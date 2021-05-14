package com.curator.backend.register.exam.category.service;

import com.curator.backend.register.exam.category.entity.dto.ExamCategoryDTO;
import com.curator.backend.register.exam.category.entity.vo.info.ExamCategoryInfo;
import com.curator.backend.register.exam.category.entity.vo.search.ExamCategorySearch;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;

import java.util.List;

/**
 * 考试类别 服务类
 *
 * @author Jun
 * @since 2021-05-13
 */
public interface ExamCategoryService {

    /**
     * 考试类别分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<PageResult<ExamCategoryDTO>> pageWithExamCategory(ExamCategorySearch search);

    /**
     * 考试类别列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<List<ExamCategoryDTO>> listWithExamCategory(ExamCategorySearch search);

    /**
     * 查询考试类别
     *
     * @param id 考试类别ID
     * @return {@link ResultResponse}
     */
    ResultResponse<ExamCategoryDTO> getExamCategory(String id);

    /**
     * 添加考试类别
     *
     * @param info 考试类别信息
     * @return {@link ResultResponse}
     */
    ResultResponse<ExamCategoryDTO> saveExamCategory(ExamCategoryInfo info);

    /**
     * 编辑考试类别
     *
     * @param info 考试类别信息
     * @return {@link ResultResponse}
     */
    ResultResponse<ExamCategoryDTO> putExamCategory(ExamCategoryInfo info);

    /**
     * 删除考试类别
     *
     * @param id 考试类别ID
     * @return {@link ResultResponse}
     */
    ResultResponse<String> removeExamCategory(String id);

}
