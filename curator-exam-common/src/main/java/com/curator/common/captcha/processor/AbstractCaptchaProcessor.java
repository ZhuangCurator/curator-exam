package com.curator.common.captcha.processor;

import cn.hutool.core.util.IdUtil;
import com.curator.common.captcha.generator.CaptchaGenerator;
import com.curator.common.properties.Captcha;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.RedissonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Jun
 * @date 2021/4/24
 */
public abstract class AbstractCaptchaProcessor implements CaptchaProcessor {

    @Autowired
    private Map<String, CaptchaGenerator> captchaGenerators;


    @Override
    public ResultResponse<HashMap<String, Object>> create(HttpServletRequest request, HttpServletResponse response) {
        Captcha captcha = generate(request);
        String uuid = saveCaptcha(captcha);
        return null;
    }

    @Override
    public ResultResponse<?> validate(HttpServletRequest request) {
        return null;
    }

    /**
     * 发送校验码  由子类实现
     *
     * @param request
     * @param uuid         缓存key的后缀
     * @param captcha
     * @return
     * @throws IOException
     */
    protected abstract ResultResponse<HashMap<String, Object>> send(HttpServletRequest request, String uuid, Captcha captcha) throws IOException;

    /**
     * 生成校验码
     *
     * @param request
     * @return
     */
    private Captcha generate(HttpServletRequest request) {
        String className = getClass().getSimpleName();
        String type = StringUtils.substringBefore(className, "CaptchaProcessor");
        String generatorName = type + CaptchaGenerator.class.getSimpleName();
        CaptchaGenerator captchaGenerator = captchaGenerators.get(generatorName);
        return  captchaGenerator.generate(request);
    }

    /**
     * 将验证码存入redis缓存中
     *
     * @param captcha  验证码对象
     *
     * @return 验证码唯一标识
     */
    private String saveCaptcha(Captcha captcha) {
        String uuid = IdUtil.fastSimpleUUID();
        RedissonUtil.setCacheObject(getRedisCacheKey(uuid), captcha.getCode(), captcha.getExpireIn(), TimeUnit.SECONDS);
        return uuid;
    }

    /**
     * 构建验证码放入REDIS时的key
     *
     * @return
     */
    private String getRedisCacheKey(String uuid) {
        return CAPTCHA_CACHE_KEY + uuid;
    }

}
