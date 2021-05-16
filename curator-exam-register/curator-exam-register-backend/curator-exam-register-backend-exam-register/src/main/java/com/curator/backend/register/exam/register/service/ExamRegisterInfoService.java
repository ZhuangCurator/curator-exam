package com.curator.backend.register.exam.register.service;

import com.curator.backend.register.exam.register.entity.dto.ExamRegisterInfoDTO;
import com.curator.backend.register.exam.register.entity.dto.ExamRegisterInfoExcelDTO;
import com.curator.backend.register.exam.register.entity.vo.search.ExamRegisterInfoSearch;
import com.curator.backend.register.exam.register.entity.vo.search.info.ExamRegisterInfoInfo;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

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

    /**
     * 生成考试口令
     *
     * @param info 考试信息
     * @return
     */
    ResultResponse<?> generateExamPassword(ExamRegisterInfoInfo info);

    /**
     * 考生统一分配教室
     *
     * @param info 考试信息
     * @return
     */
    ResultResponse<?> assignClassroom(ExamRegisterInfoInfo info);

    /**
     * 考生重考
     *
     * @param info 考试信息
     * @return
     */
    ResultResponse<?> reExam(ExamRegisterInfoInfo info);

    /**
     * 当前考点考生报名信息导出列表
     *
     * @param search 查询条件
     * @return
     */
    List<ExamRegisterInfoExcelDTO> listWithExamRegisterInfoExcel(ExamRegisterInfoSearch search);
}
