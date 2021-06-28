package com.curator.core.auth.controller;

import com.curator.common.annotation.Log;
import com.curator.common.constant.CommonConstant;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.common.util.RedissonUtil;
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
    @Log(controllerName = "CaptchaController", remark = "验证码生成")
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

    /**
     * 单独校验验证码
     *
     * @param uuid 唯一标识
     * @param captcha 验证码
     * @return
     */
    @GetMapping("/check/captcha")
    @Log(controllerName = "CaptchaController", remark = "单独校验验证码")
    public ResultResponse<?> checkCaptcha(String uuid, String captcha) {
        String redisKey = CommonConstant.CAPTCHA_CACHE_KEY + uuid;
        String codeInRedis = RedissonUtil.getCacheObject(redisKey);
        if (Help.isEmpty(codeInRedis)) {
            return ResultResponse.builder().failure("验证码已过期!").build();
        } else if (!codeInRedis.equals(captcha)) {
            return ResultResponse.builder().failure("请输入正确的验证码!").build();
        } else {
            // 校验成功 删除缓存中的验证码
            RedissonUtil.deleteObject(redisKey);
        }
        return ResultResponse.builder().success("短信验证码校验成功!").build();
    }

//    /**
//     * 单独校验短信验证码
//     *
//     * @param uuid 唯一标识
//     * @param captcha 验证码
//     * @return
//     */
//    @GetMapping("/check/smsCode")
//    @Log(controllerName = "CaptchaController", remark = "单独校验短信验证码")
//    public ResultResponse<?> checkSmsCode(String uuid, String captcha) {
//        String redisKey = CommonConstant.CAPTCHA_CACHE_KEY + uuid;
//        String codeInRedis = RedissonUtil.getCacheObject(redisKey);
//        if (Help.isEmpty(codeInRedis)) {
//            return ResultResponse.builder().failure("验证码已过期!").build();
//        } else if (!codeInRedis.equals(captcha)) {
//            return ResultResponse.builder().failure("请输入正确的验证码!").build();
//        } else {
//            // 校验成功 删除缓存中的验证码
//            RedissonUtil.deleteObject(redisKey);
//        }
//        return ResultResponse.builder().success("短信验证码校验成功!").build();
//    }
}