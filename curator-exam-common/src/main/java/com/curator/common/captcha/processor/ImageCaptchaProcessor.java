package com.curator.common.captcha.processor;

import com.curator.common.constant.CommonConstant;
import com.curator.common.properties.Captcha;
import com.curator.common.support.ResultResponse;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

/**
 * @author Jun
 * @date 2021/4/24
 */
public class ImageCaptchaProcessor extends AbstractCaptchaProcessor{


    @Override
    protected ResultResponse<HashMap<String, Object>> send(HttpServletRequest request, String uuid, Captcha captcha) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(captcha.getImage(), "jpeg", outputStream);

        HashMap<String, Object> map = new HashMap<>(8);
        map.put("imageUrl", CommonConstant.BASE64_IMAGE_PREFIX + Base64.getEncoder().encodeToString(outputStream.toByteArray()));
        map.put("uuid", uuid);
        return ResultResponse.<HashMap<String, Object>>builder().success("图片验证码生成成功").data(map).build();
    }
}
