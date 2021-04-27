package com.curator.core.auth.configure;

import com.curator.core.auth.filter.CaptchaFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jun
 * @date 2021/4/26
 */
//@Configuration
public class CaptchaConfigure {

//    @Bean
//    public FilterRegistrationBean<CaptchaFilter> filterRegistration() {
//        FilterRegistrationBean<CaptchaFilter> registration = new FilterRegistrationBean<>();
//        registration.setFilter(new CaptchaFilter());
//        // registration.addUrlPatterns("/*");
//        registration.setOrder(1);
//        return registration;
//    }
}
