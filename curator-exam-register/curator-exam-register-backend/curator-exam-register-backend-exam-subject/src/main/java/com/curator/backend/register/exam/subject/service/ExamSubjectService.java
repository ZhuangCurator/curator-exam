package com.curator.backend.register.exam.subject.service;

import com.curator.backend.register.exam.subject.entity.dto.ExamSubjectDTO;
import com.curator.backend.register.exam.subject.entity.dto.ExamSubjectSiteDTO;
import com.curator.backend.register.exam.subject.entity.vo.info.ExamSubjectInfo;
import com.curator.backend.register.exam.subject.entity.vo.info.ExamSubjectSiteInfo;
import com.curator.backend.register.exam.subject.entity.vo.search.ExamSubjectSearch;
import com.curator.backend.register.exam.subject.entity.vo.search.ExamSubjectSiteSearch;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;

import java.util.List;

/**
 * 考试科目 服务类
 *
 * @author Jun
 * @since 2021-05-13
 */
public interface ExamSubjectService {

    /**
     * 考试科目分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<PageResult<ExamSubjectDTO>> pageWithExamSubject(ExamSubjectSearch search);

    /**
     * 考试科目列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<List<ExamSubjectDTO>> listWithExamSubject(ExamSubjectSearch search);

    /**
     * 查询考试科目
     *
     * @param id 考试科目ID
     * @return {@link ResultResponse}
     */
    ResultResponse<ExamSubjectDTO> getExamSubject(String id);

    /**
     * 添加考试科目
     *
     * @param info 考试科目信息
     * @return {@link ResultResponse}
     */
    ResultResponse<ExamSubjectDTO> saveExamSubject(ExamSubjectInfo info);

    /**
     * 编辑考试科目
     *
     * @param info 考试科目信息
     * @return {@link ResultResponse}
     */
    ResultResponse<ExamSubjectDTO> putExamSubject(ExamSubjectInfo info);

    /**
     * 删除考试科目
     *
     * @param id 考试科目ID
     * @return {@link ResultResponse}
     */
    ResultResponse<String> removeExamSubject(String id);

    /**
     * 考试科目下考点分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<PageResult<ExamSubjectSiteDTO>> pageWithExamSubjectSite(ExamSubjectSiteSearch search);

    /**
     * 添加考点至考试科目
     *
     * @param info 关联信息
     * @return
     */
    ResultResponse<?> addExamSiteToSubject(ExamSubjectSiteInfo info);
}
