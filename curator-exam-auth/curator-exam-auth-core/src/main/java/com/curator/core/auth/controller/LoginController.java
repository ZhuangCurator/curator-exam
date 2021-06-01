package com.curator.core.auth.controller;

import com.curator.api.auth.pojo.dto.LoginAccountDTO;
import com.curator.api.auth.pojo.vo.LoginAccountInfo;
import com.curator.common.annotation.Log;
import com.curator.common.constant.CommonConstant;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.RedissonUtil;
import com.curator.common.util.ServletUtil;
import com.curator.core.auth.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 登录控制器
 *
 * @author Jun
 * @date 2021/4/18
 */
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 账户登录
     *
     * @param info 账户名、密码等
     * @return 登录后的唯一标识
     */
    @PostMapping("/login/account")
    @Log(controllerName = "LoginController", remark = "账户密码登录")
    public ResultResponse<Map<String, Object>> login(LoginAccountInfo info) {
       return loginService.login(info);
    }

    // /**
    //  * 手机号登录
    //  *
    //  * @param info 账户名、密码等
    //  * @return 登录后的唯一标识
    //  */
    // @PostMapping("/login/phone")
    // @Log(controllerName = "LoginController", remark = "账户登录")
    // public ResultResponse<Map<String, Object>> login(LoginAccountInfo info) {
    //     return loginService.login(info);
    // }

    /**
     * 账户注销
     *
     * @param request 请求参数
     * @return {@link ResultResponse}
     */
    @DeleteMapping("/logout")
    @Log(controllerName = "LoginController", remark = "账户注销")
    public ResultResponse<?> logout(HttpServletRequest request) {
        return loginService.logout(request);
    }

    /**
     * 获取登录账户
     *
     * @return {@link ResultResponse}
     */
    @GetMapping("/loginAccount")
    @Log(controllerName = "LoginController", remark = "获取登录账户")
    public ResultResponse<LoginAccountDTO> getLoginAccount() {
        String token = ServletUtil.getRequest().getHeader(CommonConstant.TOKEN_HEADER);
        token = token.replaceAll(CommonConstant.TOKEN_PREFIX, "");
        LoginAccountDTO accountDTO = RedissonUtil.getCacheObject(CommonConstant.CACHE_ACCOUNT_PREFIX + token);
        return ResultResponse.<LoginAccountDTO>builder().success("登录账户获取成功！").data(accountDTO).build();
    }
}
