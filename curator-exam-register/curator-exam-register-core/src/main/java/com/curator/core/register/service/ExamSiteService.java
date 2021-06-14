package com.curator.core.register.service;

import com.curator.api.register.pojo.dto.ExamSiteDTO;
import com.curator.api.register.pojo.vo.search.ExamSiteSearch;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;

/**
 * 考点 服务类
 *
 * @author Jun
 * @date 2021/5/17
 */
public interface ExamSiteService {

    /**
     * 考点分页查询
     *
     * @param search 查询条件
     * @return
     */
    ResultResponse<PageResult<ExamSiteDTO>> pageWithExamSite(ExamSiteSearch search);
}
