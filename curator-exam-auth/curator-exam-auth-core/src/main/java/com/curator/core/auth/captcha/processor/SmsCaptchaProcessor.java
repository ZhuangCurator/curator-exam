package com.curator.core.auth.captcha.processor;

import com.curator.api.auth.pojo.dto.CaptchaDTO;
import com.curator.common.constant.CommonConstant;
import com.curator.common.support.ResultResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

/**
 * 短信验证码处理器
 *
 * @author Jun
 * @date 2021/4/24
 */
@Component
public class SmsCaptchaProcessor extends AbstractCaptchaProcessor {

    @Override
    protected ResultResponse<HashMap<String, Object>> send(HttpServletRequest request, String uuid, CaptchaDTO captcha) throws Exception {
        String paramName = "mobile";
        String mobile = ServletRequestUtils.getRequiredStringParameter(request, paramName);
        System.out.println("短信已发送了，哈哈哈！手机号为：" + mobile + ",验证码为：" + captcha.getCode());
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("uuid", uuid);
        return ResultResponse.<HashMap<String, Object>>builder().success("手机验证码发送成功").data(map).build();
    }

}
