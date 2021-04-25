package com.curator.core.auth.captcha.processor;

import com.curator.api.auth.pojo.dto.CaptchaDTO;
import com.curator.common.constant.CommonConstant;
import com.curator.common.support.ResultResponse;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

/**
 * 图片验证码处理器
 *
 * @author Jun
 * @date 2021/4/24
 */
@Component
public class ImageCaptchaProcessor extends AbstractCaptchaProcessor {

    @Override
    protected ResultResponse<HashMap<String, Object>> send(HttpServletRequest request, String uuid, CaptchaDTO captcha) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(captcha.getImage(), "jpeg", outputStream);

        HashMap<String, Object> map = new HashMap<>(8);
        map.put("imageUrl", CommonConstant.BASE64_IMAGE_PREFIX + Base64.getEncoder().encodeToString(outputStream.toByteArray()));
        map.put("uuid", uuid);
        return ResultResponse.<HashMap<String, Object>>builder().success("图片验证码生成成功").data(map).build();
    }

}
