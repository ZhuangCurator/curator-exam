package com.curator.core.auth.captcha.generator;

import cn.hutool.core.util.RandomUtil;
import com.curator.api.auth.pojo.dto.CaptchaDTO;
import com.curator.core.auth.properties.CaptchaProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;

/**
 * 短信验证码生成器
 *
 * @author Jun
 * @date 2021/4/23
 */
@Component
public class SmsCaptchaGenerator extends CaptchaGenerator {

    private CaptchaProperties captchaProperties;

    @Override
    public CaptchaDTO generate(HttpServletRequest request) {
        String randomNum = RandomUtil.randomNumbers(captchaProperties.getLength());
        return new CaptchaDTO(null, randomNum, captchaProperties.getExpire());
    }

    public SmsCaptchaGenerator(CaptchaProperties captchaProperties) {
        this.captchaProperties = captchaProperties;
    }
}
