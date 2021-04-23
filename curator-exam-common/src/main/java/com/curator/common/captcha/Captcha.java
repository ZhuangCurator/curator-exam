package com.curator.common.captcha;

import java.awt.*;
import java.util.Random;

/**
 * 验证码抽象类
 *
 * @author Jun
 * @date 2021/4/23
 */
public abstract class Captcha {

    /**
     * 生成随机颜色
     *
     * @param fc 0-255
     * @param bc 0-255
     * @return 随机颜色
     */
    public Color getRandColor(int fc, int bc) {
        Random random = new Random();
        int limit = 255;
        if (fc > limit) {
            fc = limit;
        }
        if (bc > limit) {
            bc = limit;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }


}
