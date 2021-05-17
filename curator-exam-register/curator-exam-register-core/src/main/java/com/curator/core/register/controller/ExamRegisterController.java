package com.curator.core.register.controller;

import com.curator.api.register.pojo.vo.info.ExamRegisterInfoInfo;
import com.curator.common.annotation.Log;
import com.curator.common.support.ResultResponse;
import com.curator.core.register.service.ExamRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    ResultResponse<String> accountRegister(ExamRegisterInfoInfo info) {
        return examRegisterService.accountRegister(info);
    }
}
