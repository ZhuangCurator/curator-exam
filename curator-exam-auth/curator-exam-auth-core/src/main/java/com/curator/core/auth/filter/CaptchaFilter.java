package com.curator.core.auth.filter;

import com.curator.common.util.Help;
import com.curator.core.auth.captcha.holder.CaptchaProcessorHolder;
import com.curator.core.auth.exception.CaptchaException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 * 验证码过滤器
 *
 * @author Jun
 * @date 2021/4/26
 */
@WebFilter
public class CaptchaFilter implements Filter, InitializingBean {

    @Autowired
    private CaptchaProcessorHolder captchaProcessorHolder;

    private final static String LOGIN_URL = "/login";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 若该请求对应的验证码类型为空，则表明该请求不需要校验，直接放行
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String type = getCaptchaType(httpServletRequest);
        if (Help.isNotEmpty(type)) {
            try {
                captchaProcessorHolder.findValidateCodeProcessor(type).validate(httpServletRequest);
            } catch (CaptchaException exception) {
                // 将错误信息封装在request中
                request.setAttribute("message", exception.getMessage());
                // 请求转发
                request.getRequestDispatcher("/captcha/exception").forward(request, response);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    /**
     * 存放所有需要校验验证码的url
     */
    private HashMap<String, String> urlMap = new HashMap<>(8);

    @Override
    public void afterPropertiesSet() throws Exception {
        urlMap.put("/login/account", "image");
        urlMap.put("/login/phone", "sms");
    }

    /**
     * 若当前uri需要校验验证码，则返回对应的验证码类型，否则 返回 null
     *
     * @param request
     * @return
     */
    private String getCaptchaType(HttpServletRequest request) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String result;
        String method = "GET";
        if (!StringUtils.equalsIgnoreCase(request.getMethod(), method)) {
            Set<String> urls = urlMap.keySet();
            for (String url : urls) {
                if (pathMatcher.match(url, request.getRequestURI())) {
                    result = urlMap.get(url);
                    return result;
                }
            }
        }
        return null;
    }
}
