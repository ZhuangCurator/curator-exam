package com.curator.backend.register.exam.site.service;

import com.curator.backend.register.exam.site.entity.dto.ExamSiteDTO;
import com.curator.backend.register.exam.site.entity.vo.info.ExamSiteInfo;
import com.curator.backend.register.exam.site.entity.vo.search.ExamSiteSearch;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;

import java.util.List;

/**
 * 考点 服务类
 *
 * @author Jun
 * @since 2021-05-13
 */
public interface ExamSiteService {

    /**
     * 考点分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<PageResult<ExamSiteDTO>> pageWithExamSite(ExamSiteSearch search);

    /**
     * 考点列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<List<ExamSiteDTO>> listWithExamSite(ExamSiteSearch search);

    /**
     * 查询考点
     *
     * @param id 考点ID
     * @return {@link ResultResponse}
     */
    ResultResponse<ExamSiteDTO> getExamSite(String id);

    /**
     * 添加考点
     *
     * @param info 考点信息
     * @return {@link ResultResponse}
     */
    ResultResponse<ExamSiteDTO> saveExamSite(ExamSiteInfo info);

    /**
     * 编辑考点
     *
     * @param info 考点信息
     * @return {@link ResultResponse}
     */
    ResultResponse<ExamSiteDTO> putExamSite(ExamSiteInfo info);

    /**
     * 删除考点
     *
     * @param id 考点ID
     * @return {@link ResultResponse}
     */
    ResultResponse<String> removeExamSite(String id);
}
