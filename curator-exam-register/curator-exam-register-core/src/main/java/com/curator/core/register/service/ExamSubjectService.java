package com.curator.core.register.service;

import com.curator.api.register.pojo.dto.ExamSubjectDTO;
import com.curator.api.register.pojo.vo.search.ExamSubjectSearch;
import com.curator.common.support.ResultResponse;

import java.util.List;

/**
 * 考试科目 服务类
 *
 * @author Jun
 * @date 2021/5/17
 */
public interface ExamSubjectService {

    /**
     * 考试科目列表查询
     *
     * @param search 查询条件
     * @return
     */
    ResultResponse<List<ExamSubjectDTO>> listWithExamSubject(ExamSubjectSearch search);
}
