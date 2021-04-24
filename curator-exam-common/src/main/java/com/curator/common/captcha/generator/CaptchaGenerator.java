package com.curator.common.captcha.generator;

import com.curator.common.properties.Captcha;
import com.curator.common.properties.CaptchaProperties;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * 验证码生成器
 *
 * @author Jun
 * @date 2021/4/23
 */
public abstract class CaptchaGenerator {

    /**
     * 生成验证码逻辑接口
     *
     * @param request http请求
     * @return 验证码对象
     */
    public abstract Captcha generate(HttpServletRequest request);

    /**
     * 获取图片宽度
     *
     * @return
     */
    public int width(CaptchaProperties captchaProperties, HttpServletRequest request){
        return ServletRequestUtils.getIntParameter(request,"width",captchaProperties.getWidth());
    }

    /**
     * 获取图片高度
     *
     * @return
     */
    public int height(CaptchaProperties captchaProperties, HttpServletRequest request){
        return ServletRequestUtils.getIntParameter(request,"height",captchaProperties.getWidth());
    }

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

    /**
     * 绘制图片
     *
     * @param len    字符长度
     * @param width  图片宽度
     * @param height 图片高度
     * @param str 字符串
     * @return
     */
    public BufferedImage createImage(int len, int width, int height, String str) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();
        Random random = new Random();
        // 填充背景
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ITALIC, 30));
        // 随机画干扰线
        int num = 155;
        IntStream.range(0, num).map(i1 -> random.nextInt(width)).forEach(x -> {
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        });

        // 居中显示字符
        FontMetrics fm = g.getFontMetrics();
        int x = ((width - fm.stringWidth(str)) / 2);
        int y = ((height - fm.getHeight()) / 2) + fm.getAscent();
        String[] strArr = str.split("");
        for (int i = 0; i < strArr.length; i++) {
            g.setColor(new Color(1 + random.nextInt(250), 1 + random.nextInt(250), 1 + random.nextInt(250)));
            g.drawString(strArr[i], x + (i * fm.stringWidth(strArr[i])), y);
        }
        g.dispose();

        return image;
    }
}
