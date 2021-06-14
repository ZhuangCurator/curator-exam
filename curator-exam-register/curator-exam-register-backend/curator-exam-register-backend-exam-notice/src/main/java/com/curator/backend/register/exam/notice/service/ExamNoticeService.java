package com.curator.backend.register.exam.notice.service;

import com.curator.backend.register.exam.notice.entity.dto.ExamNoticeDTO;
import com.curator.backend.register.exam.notice.entity.vo.info.ExamNoticeInfo;
import com.curator.backend.register.exam.notice.entity.vo.search.ExamNoticeSearch;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;

import java.util.List;

/**
 * 考试公告 服务类
 *
 * @author Jun
 * @since 2021-05-13
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
     * 考试公告列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<List<ExamNoticeDTO>> listWithExamNotice(ExamNoticeSearch search);

    /**
     * 查询考试公告
     *
     * @param id 考试公告ID
     * @return {@link ResultResponse}
     */
    ResultResponse<ExamNoticeDTO> getExamNotice(String id);

    /**
     * 添加考试公告
     *
     * @param info 考试公告信息
     * @return {@link ResultResponse}
     */
    ResultResponse<ExamNoticeDTO> saveExamNotice(ExamNoticeInfo info);

    /**
     * 编辑考试公告
     *
     * @param info 考试公告信息
     * @return {@link ResultResponse}
     */
    ResultResponse<ExamNoticeDTO> putExamNotice(ExamNoticeInfo info);

    /**
     * 删除考试公告
     *
     * @param id 考试公告ID
     * @return {@link ResultResponse}
     */
    ResultResponse<String> removeExamNotice(String id);

}
