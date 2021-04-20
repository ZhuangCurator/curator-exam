package com.curator.exam.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 网关url过滤属性配置
 *
 * @author Jun
 * @date 2021/4/17
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "curator.gateway.url")
public class GatewayUrlProperties {

    /**
     * 白名单配置，网关不校验此处的白名单
     */
    private List<String> whites = new ArrayList<>();

    /**
     * 黑名单配置，禁止访问此处的黑名单
     */
    private List<String> blacks = new ArrayList<>();
}
