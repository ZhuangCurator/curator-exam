package com.curator.core.register.service;

import com.curator.api.register.pojo.dto.ExamNoticeDTO;
import com.curator.api.register.pojo.vo.search.ExamNoticeSearch;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;

/**
 * 考试公告 服务类
 *
 * @author Jun
 * @date 2021/6/14
 */
public interface ExamNoticeService {

    /**
     * 考试公告分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<PageResult<ExamNoticeDTO>> pageWithExamNotice(ExamNoticeSearch search);

    /**
     * 查询考试公告
     *
     * @param id 考试公告ID
     * @return {@link ResultResponse}
     */
    ResultResponse<ExamNoticeDTO> getExamNotice(String id);
}
