package com.curator.core.auth.service;

import com.curator.api.auth.pojo.vo.LoginAccountInfo;
import com.curator.common.support.ResultResponse;

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
    ResultResponse<String> login(LoginAccountInfo info);
}
