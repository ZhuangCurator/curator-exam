package com.curator.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码配置属性
 *
 * @author Jun
 * @date 2021/4/23
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "captcha")
public class CaptchaProperties {

    /**
     * 验证码字符长度
     */
    private String length;

    /**
     * 验证码图片宽度
     */
    private String width;

    /**
     * 验证码图片高度
     */
    private String height;

    /**
     * 验证码过期时间(单位: 秒)
     */
    private Long expire;
}
