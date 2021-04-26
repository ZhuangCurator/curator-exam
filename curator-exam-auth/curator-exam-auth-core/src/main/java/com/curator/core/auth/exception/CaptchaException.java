package com.curator.core.auth.exception;

/**
 * 自定义验证码异常
 *
 * @author Jun
 * @date 2021/4/26
 */
public class CaptchaException extends RuntimeException{

    public CaptchaException(String msg){
        super(msg);
    }
}
