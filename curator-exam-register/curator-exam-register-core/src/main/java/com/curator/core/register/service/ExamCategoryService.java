package com.curator.core.register.service;

import com.curator.api.register.pojo.dto.ExamCategoryDTO;
import com.curator.api.register.pojo.vo.search.ExamCategorySearch;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;

import java.util.List;

/**
 * 考试类别 服务类
 *
 * @author Jun
 * @date 2021/5/17
 */
public interface ExamCategoryService {

    /**
     * 考试类别列表查询
     *
     * @param search 查询条件
     * @return
     */
    ResultResponse<PageResult<ExamCategoryDTO>> pageWithExamCategory(ExamCategorySearch search);
}
