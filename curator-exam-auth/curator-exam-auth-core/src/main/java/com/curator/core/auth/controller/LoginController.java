package com.curator.core.auth.controller;

import com.curator.common.support.ResultResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录控制器
 *
 * @author Jun
 * @date 2021/4/18
 */
@RestController
public class LoginController {

    @PostMapping("/login")
   public ResultResponse<String> login() {
       return null;
   }
}
