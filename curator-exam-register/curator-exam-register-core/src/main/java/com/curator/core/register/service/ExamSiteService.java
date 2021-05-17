package com.curator.core.register.service;

import com.curator.api.register.pojo.dto.ExamSiteDTO;
import com.curator.api.register.pojo.vo.search.ExamSiteSearch;
import com.curator.common.support.ResultResponse;

import java.util.List;

/**
 * 考点 服务类
 *
 * @author Jun
 * @date 2021/5/17
 */
public interface ExamSiteService {

    /**
     * 考点列表查询
     *
     * @param search 查询条件
     * @return
     */
    ResultResponse<List<ExamSiteDTO>> listWithExamSite(ExamSiteSearch search);
}
