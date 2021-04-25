package com.curator.core.auth.captcha.generator;

import cn.hutool.core.util.RandomUtil;
import com.curator.api.auth.pojo.dto.CaptchaDTO;
import com.curator.core.auth.properties.CaptchaProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;

/**
 * 数字验证码生成器
 *
 * @author Jun
 * @date 2021/4/23
 */
@Component
public class NumberCaptchaGenerator extends CaptchaGenerator {

    private CaptchaProperties captchaProperties;

    @Override
    public CaptchaDTO generate(HttpServletRequest request) {
        // 可以在页面上配置图片验证码的长宽
        int width = width(captchaProperties, request);
        int height = height(captchaProperties, request);

        String randomNum = RandomUtil.randomNumbers(captchaProperties.getLength());
        BufferedImage image = createImage(captchaProperties.getLength(), width, height, randomNum);

        return new CaptchaDTO(image, randomNum, captchaProperties.getExpire());
    }


    public NumberCaptchaGenerator(CaptchaProperties captchaProperties) {
        this.captchaProperties = captchaProperties;
    }
}
