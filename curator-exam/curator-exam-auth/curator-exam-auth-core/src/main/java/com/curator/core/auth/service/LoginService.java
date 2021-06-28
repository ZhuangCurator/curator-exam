package com.curator.core.auth.service;

import com.curator.api.auth.pojo.vo.LoginAccountInfo;
import com.curator.common.support.ResultResponse;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 登录服务接口
 *
 * @author Jun
 * @date 2021/4/19
 */
public interface LoginService {

    /**
     * 登录方法
     *
     * @param info 账户信息
     * @return
     */
    ResultResponse<Map<String, Object>> login(LoginAccountInfo info);

    /**
     * 账户注销
     *
     * @param request 请求参数
     * @return
     */
    ResultResponse<?> logout(HttpServletRequest request);
}
