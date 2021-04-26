package com.curator.core.auth.captcha.processor;

import cn.hutool.core.util.IdUtil;
import com.curator.api.auth.pojo.dto.CaptchaDTO;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.common.util.RedissonUtil;
import com.curator.core.auth.captcha.generator.CaptchaGenerator;
import com.curator.core.auth.exception.CaptchaException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

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
    public void validate(HttpServletRequest request) {
        String uuid = ServletRequestUtils.getStringParameter(request, "uuid", "");
        if (Help.isEmpty(uuid)) {
            throw new CaptchaException("验证码唯一标识不能为空!");
        }
        String codeInRedis = RedissonUtil.getCacheObject(getRedisCacheKey(uuid));
        String codeInRequest = ServletRequestUtils.getStringParameter(request,
                "captcha", "");
        if (Help.isEmpty(codeInRequest)) {
            throw new CaptchaException("验证码的值不能为空!");
        } else if (Help.isEmpty(codeInRedis)) {
            throw new CaptchaException("验证码已过期!");
        } else if (!codeInRedis.equals(codeInRequest)) {
            throw new CaptchaException("请输入正确的验证码!");
        } else {
            // 校验成功 删除缓存中的验证码
            RedissonUtil.deleteObject(getRedisCacheKey(uuid));
        }
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
     * @param generator 生成器简称，若一个处理器对应多个生成器，则由此判断使用哪个生成器
     * @return
     */
    private CaptchaDTO generate(HttpServletRequest request, String generator) {
        String generatorName;
        if (Help.isNotEmpty(generator)) {
            generatorName = generator.trim().toLowerCase() + CaptchaGenerator.class.getSimpleName();
        } else {
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
