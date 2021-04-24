package com.curator.common.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * 验证码对象
 *
 * @author Jun
 * @date 2021/4/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Captcha implements Serializable {

    /**
     * 图片对象
     */
    private BufferedImage image;

    /**
     * 验证码
     */
    private String code;

    /**
     * 过期时间
     */
    private long expireIn;

}
