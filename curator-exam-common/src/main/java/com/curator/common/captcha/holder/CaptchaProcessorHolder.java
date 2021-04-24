package com.curator.common.captcha.holder;

import com.curator.common.captcha.processor.CaptchaProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author Jun
 * @date 2021/4/24
 */
public class CaptchaProcessorHolder {

    @Autowired
    private Map<String, CaptchaProcessor> validateCodeProcessors;




}
