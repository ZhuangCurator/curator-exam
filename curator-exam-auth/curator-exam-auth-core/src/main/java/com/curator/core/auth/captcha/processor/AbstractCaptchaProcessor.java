package com.curator.core.auth.captcha.processor;

import cn.hutool.core.util.IdUtil;
import com.curator.api.auth.pojo.dto.CaptchaDTO;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.common.util.RedissonUtil;
import com.curator.core.auth.captcha.generator.CaptchaGenerator;
import com.curator.core.auth.captcha.generator.SmsCaptchaGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public ResultResponse<HashMap<String, Object>> create(HttpServletRequest request, HttpServletResponse response, String generator) throws Exception {
        CaptchaDTO captcha = generate(request, generator);
        String uuid = saveCaptcha(captcha);
        return send(request, uuid, captcha);
    }

    @Override
    public ResultResponse<?> validate(HttpServletRequest request) {
        return null;
    }

    /**
     * 发送校验码  由子类实现
     *
     * @param request
     * @param uuid    缓存key的后缀
     * @param captcha
     * @return
     * @throws Exception
     */
    protected abstract ResultResponse<HashMap<String, Object>> send(HttpServletRequest request, String uuid, CaptchaDTO captcha) throws Exception;

    /**
     * 生成校验码
     *
     * @param request
     *
     * @param generator 生成器简称，若一个处理器对应多个生成器，则由此判断使用哪个生成器
     * @return
     */
    private CaptchaDTO generate(HttpServletRequest request, String generator) {
        String generatorName;
        if(Help.isNotEmpty(generator)) {
            generatorName = generator.trim().toLowerCase() + CaptchaGenerator.class.getSimpleName();
        }else {
            String className = getClass().getSimpleName();
            String type = StringUtils.substringBefore(className, "CaptchaProcessor").toLowerCase();
            generatorName = type + CaptchaGenerator.class.getSimpleName();
        }
        CaptchaGenerator captchaGenerator = captchaGenerators.get(generatorName);
        return captchaGenerator.generate(request);
    }

    /**
     * 将验证码存入redis缓存中
     *
     * @param captcha 验证码对象
     * @return 验证码唯一标识
     */
    private String saveCaptcha(CaptchaDTO captcha) {
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
