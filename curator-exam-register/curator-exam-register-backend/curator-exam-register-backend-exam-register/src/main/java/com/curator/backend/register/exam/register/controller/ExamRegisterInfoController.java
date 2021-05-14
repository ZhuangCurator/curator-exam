package com.curator.backend.register.exam.register.controller;

import com.curator.backend.register.exam.register.entity.dto.ExamRegisterInfoDTO;
import com.curator.backend.register.exam.register.entity.vo.search.ExamRegisterInfoSearch;
import com.curator.backend.register.exam.register.service.ExamRegisterInfoService;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 考试报名信息 前端控制器
 *
 * @author Jun
 * @since 2021-05-13
 */
@RestController
@RequestMapping("/examRegisterInfo")
public class ExamRegisterInfoController {

    @Autowired
    private ExamRegisterInfoService examRegisterInfoService;

    /**
     * 考试报名信息分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/page")
    @Log(controllerName = "ExamRegisterInfoController", remark = "考试报名信息分页查询")
    ResultResponse<PageResult<ExamRegisterInfoDTO>> pageWithExamRegisterInfo(ExamRegisterInfoSearch search) {
        return examRegisterInfoService.pageWithExamRegisterInfo(search);
    }

}
