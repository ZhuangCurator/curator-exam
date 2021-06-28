package com.curator.core.auth.captcha.holder;

import com.curator.core.auth.captcha.processor.CaptchaProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Jun
 * @date 2021/4/24
 */
@Slf4j
@Component
public class CaptchaProcessorHolder {

    @Autowired
    private Map<String, CaptchaProcessor> validateCodeProcessors;

    public CaptchaProcessor findValidateCodeProcessor(String type) {
        String name = type.toLowerCase() + CaptchaProcessor.class.getSimpleName();
        CaptchaProcessor processor = validateCodeProcessors.get(name);
        if (processor == null) {
            log.error("验证码处理器" + name + "不存在");
        }
        return processor;
    }

}
