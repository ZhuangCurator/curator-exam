package com.curator.core.auth.controller;

import com.curator.common.support.ResultResponse;
import com.curator.core.auth.captcha.holder.CaptchaProcessorHolder;
import com.curator.core.auth.captcha.processor.CaptchaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * 验证码控制器
 *
 * @author Jun
 * @date 2021/4/25
 */
@RestController
public class CaptchaController {

    @Autowired
    private CaptchaProcessorHolder captchaProcessorHolder;

    /**
     * 创建验证码，根据验证码类型不同 ，调用不同的 {@link CaptchaProcessor} 接口实现
     * /captcha/sms /captcha/image/number  /captcha/image/chinese
     *
     * @param request
     * @param response
     * @param type 验证码类型： image或者sms
     * @param generator 处理器简称： chinese或者number
     * @throws Exception
     */
    @GetMapping(value = { "/captcha/{type}", "/captcha/{type}/{generator}" })
    public ResultResponse<HashMap<String,Object>> createCode(HttpServletRequest request, HttpServletResponse response,
            @PathVariable("type") String type,  @PathVariable(value = "generator", required = false) String generator) throws Exception{
        return captchaProcessorHolder.findValidateCodeProcessor(type).create(request, response, generator);
    }

    /**
     * 验证码异常处理
     *
     * @param request 请求
     * @return
     */
    @PostMapping("/captcha/exception")
    public ResultResponse<?> exception(HttpServletRequest request) {
        String message = (String) request.getAttribute("message");
        return ResultResponse.builder().failure(message).build();
    }
}