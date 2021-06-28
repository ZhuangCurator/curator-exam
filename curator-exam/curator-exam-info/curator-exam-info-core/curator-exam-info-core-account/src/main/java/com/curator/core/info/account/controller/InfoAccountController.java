package com.curator.core.info.account.controller;

import com.curator.api.info.pojo.dto.AccountDTO;
import com.curator.api.info.pojo.vo.AccountInfo;
import com.curator.common.annotation.Log;
import com.curator.common.support.ResultResponse;
import com.curator.core.info.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 账户 前端控制器
 *
 * @author Jun
 * @since 2021-04-16
 */
@RestController
@RequestMapping("/infoAccount")
public class InfoAccountController {

    @Autowired
    private AccountService accountService;

    /**
     * 普通账户注册
     *
     * @param info 账户信息
     * @return {@link ResultResponse}
     */
    @PostMapping("/register")
    @Log(controllerName = "InfoAccountController", remark = "普通账户注册")
    ResultResponse<AccountDTO> saveInfoAccount(@RequestBody AccountInfo info) {
        return accountService.saveInfoAccount(info);
    }

    /**
     * 修改账户密码或邮箱或手机号
     *
     * @param info 账户信息
     * @return
     */
    @PutMapping("/update")
    @Log(controllerName = "InfoAccountController", remark = "修改账户密码或邮箱")
    ResultResponse<?> updateAccount(@RequestBody AccountInfo info) {
        return accountService.updateAccount(info);
    }

}
