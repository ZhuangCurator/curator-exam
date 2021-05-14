package com.curator.backend.register.exam.register.service;

import com.curator.backend.register.exam.register.entity.dto.ExamRegisterInfoDTO;
import com.curator.backend.register.exam.register.entity.vo.search.ExamRegisterInfoSearch;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;

/**
 * 考试报名信息 服务类
 *
 * @author Jun
 * @since 2021-05-13
 */
public interface ExamRegisterInfoService  {

    /**
     * 考试报名信息分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<PageResult<ExamRegisterInfoDTO>> pageWithExamRegisterInfo(ExamRegisterInfoSearch search);

}
