package com.curator.core.auth.captcha.processor;

import com.curator.common.support.ResultResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @author Jun
 * @date 2021/4/24
 */
public interface CaptchaProcessor {

    String CAPTCHA_CACHE_KEY = "captcha:cache:";

    /**
     * 创建验证码
     *
     * @param request
     * @param response
     * @return
     */
    ResultResponse<HashMap<String, Object>> create(HttpServletRequest request, HttpServletResponse response, String generator) throws Exception;

    /**
     * 校验验证码
     *
     * @param request
     * @return
     */
    ResultResponse<?> validate(HttpServletRequest request);
}
