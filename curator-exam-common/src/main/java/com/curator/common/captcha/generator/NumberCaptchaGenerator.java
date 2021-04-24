package com.curator.common.captcha.generator;

import cn.hutool.core.util.RandomUtil;
import com.curator.common.properties.Captcha;
import com.curator.common.properties.CaptchaProperties;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;

/**
 * 数字验证码生成器
 *
 * @author Jun
 * @date 2021/4/23
 */
public class NumberCaptchaGenerator extends CaptchaGenerator {

    private CaptchaProperties captchaProperties;

    @Override
    public Captcha generate(HttpServletRequest request) {
        // 可以在页面上配置图片验证码的长宽
        int width = width(captchaProperties, request);
        int height = height(captchaProperties, request);

        String randomNum = RandomUtil.randomNumbers(captchaProperties.getLength());
        BufferedImage image = createImage(captchaProperties.getLength(), width, height, randomNum);

        return new Captcha(image, randomNum, captchaProperties.getExpire());
    }


    public NumberCaptchaGenerator(CaptchaProperties captchaProperties) {
        this.captchaProperties = captchaProperties;
    }
}
