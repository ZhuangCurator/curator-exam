package com.curator.core.register.controller;

import com.curator.api.register.pojo.dto.ExamRegisterInfoDTO;
import com.curator.api.register.pojo.vo.info.ExamRegisterInfoInfo;
import com.curator.api.register.pojo.vo.search.ExamRegisterInfoSearch;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.core.register.service.ExamRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 考生报名信息 前端控制器
 *
 * @author Jun
 * @date 2021/5/17
 */
@RestController
@RequestMapping("/examRegisterInfo")
public class ExamRegisterController {

    @Autowired
    private ExamRegisterService examRegisterService;

    /**
     * 考生报名
     *
     * @param info 报名信息
     * @return
     */
    @PostMapping("/register")
    @Log(controllerName = "ExamRegisterController", remark = "考生报名")
    ResultResponse<String> accountRegister(@RequestBody ExamRegisterInfoInfo info) {
        return examRegisterService.accountRegister(info);
    }

    /**
     * 个人报名信息
     *
     * @param search 分页条件
     * @return
     */
    @GetMapping("/pageWithRegisterInfo")
    @Log(controllerName = "ExamRegisterController", remark = "个人报名信息")
    ResultResponse<PageResult<ExamRegisterInfoDTO>> pageWithRegisterInfo(ExamRegisterInfoSearch search) {
        return examRegisterService.pageWithRegisterInfo(search);
    }
}
