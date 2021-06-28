package com.curator.exam.gateway.filter;

import com.curator.api.auth.pojo.dto.LoginAccountDTO;
import com.curator.common.constant.CommonConstant;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.common.util.JsonUtil;
import com.curator.common.util.RedissonUtil;
import com.curator.exam.gateway.properties.GatewayUrlProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;

/**
 * 网关请求过滤器
 *
 * @author Jun
 * @date 2020/9/29
 */
@SuppressWarnings("rawtypes")
@Slf4j
@Component
public class AuthFilter implements GlobalFilter {

    @Autowired
    private GatewayUrlProperties gatewayUrlProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 验证黑名单
        Mono<Void> checkBlackUriResult = checkBlackUri(exchange);
        if (checkBlackUriResult != null) {
            return checkBlackUriResult;
        }
        // 保存访问日志
        accessLog(exchange);
        LinkedHashSet<URI> uriSet = exchange.getAttribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        if (Help.isNotEmpty(uriSet)) {
            // 验证白名单
            List<String> whiteUriList = gatewayUrlProperties.getWhites();
            URI originUri = uriSet.stream().findFirst().orElse(null);
            if (Help.matches(originUri.getPath(), whiteUriList)) {
                return chain.filter(handleServerWebExchange(exchange));
            }
        }
        // 获取token
        String token = exchange.getRequest().getHeaders().getFirst(CommonConstant.TOKEN_HEADER);
        if (Help.isEmpty(token)) {
            ResultResponse resultResponse = ResultResponse.builder().failure("令牌不能为空").build();
            return makeResponse(exchange, resultResponse);
        } else {
            token = token.replaceAll(CommonConstant.TOKEN_PREFIX, "");
            LoginAccountDTO accountDTO = RedissonUtil.getCacheObject(CommonConstant.CACHE_ACCOUNT_PREFIX + token);
            if (Help.isEmpty(accountDTO)) {
                ResultResponse resultResponse = ResultResponse.builder().failure("登录状态已过期").build();
                return makeResponse(exchange, resultResponse);
            }
            if (Help.isEmpty(accountDTO.getAccountName()) || Help.isEmpty(accountDTO.getAccountId())) {
                ResultResponse resultResponse = ResultResponse.builder().failure("令牌验证失败").build();
                return makeResponse(exchange, resultResponse);
            }
            // 刷新账户登录过期时间
            RedissonUtil.expire(CommonConstant.CACHE_ACCOUNT_PREFIX + token,
                    CommonConstant.TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
            // 设置用户信息到请求
            String accountName = new String(Base64Utils.encode(accountDTO.getAccountName().getBytes(StandardCharsets.UTF_8)));
            ServerWebExchange webExchange = handleServerWebExchange(exchange);
            ServerHttpRequest mutableReq = webExchange.getRequest().mutate()
                    .header(CommonConstant.HTTP_HEADER_ACCOUNT_ID, accountDTO.getAccountId())
                    .header(CommonConstant.HTTP_HEADER_ACCOUNT_PARENT_ID, accountDTO.getParentAccountId())
                    .header(CommonConstant.HTTP_HEADER_ACCOUNT_CHILDREN_ID, JsonUtil.obj2String(accountDTO.getChildrenAccountIdList()))
                    .header(CommonConstant.HTTP_HEADER_ACCOUNT_PROVINCE, accountDTO.getProvince())
                    .header(CommonConstant.HTTP_HEADER_ACCOUNT_CITY, accountDTO.getCity())
                    .header(CommonConstant.HTTP_HEADER_ACCOUNT_NAME, accountName)
                    .build();
            ServerWebExchange mutableExchange = webExchange.mutate().request(mutableReq).build();
            return chain.filter(mutableExchange);
        }
    }

    /**
     * 黑名单检查
     *
     * @param exchange
     * @return
     */
    private Mono<Void> checkBlackUri(ServerWebExchange exchange) {
        LinkedHashSet<URI> uriSet = exchange.getAttribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        AtomicBoolean shouldForward = new AtomicBoolean(true);
        if (Help.isNotEmpty(uriSet)) {
            URI originUri = uriSet.stream().findFirst().orElse(null);
            List<String> blackUriList = gatewayUrlProperties.getBlacks();
            if (Help.isNotEmpty(originUri.getPath()) && Help.isNotEmpty(blackUriList)) {
                if (Help.matches(originUri.getPath(), blackUriList)) {
                    shouldForward.set(false);
                }
            }
        }
        if (!shouldForward.get()) {
            ResultResponse resultResponse = ResultResponse.builder().failure("该URI不允许外部访问").build();
            return makeResponse(exchange, resultResponse);
        }
        return null;
    }

    private Mono<Void> makeResponse(ServerWebExchange exchange, ResultResponse resultResponse) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.setStatusCode(HttpStatus.OK);
        DataBuffer dataBuffer = response.bufferFactory().wrap(JsonUtil.obj2String(resultResponse).getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }

    /**
     * 输出网关访问日志
     *
     * @param exchange
     */
    private void accessLog(ServerWebExchange exchange) {
        URI url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
        LinkedHashSet<URI> uris = exchange.getAttribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        URI originUri = null;
        if (uris != null) {
            originUri = uris.stream().findFirst().orElse(null);
        }
        if (url != null && route != null && originUri != null) {
            log.info("转发请求：{}://{}{} --> 目标服务：{}，目标地址：{}://{}{}，转发时间：{}",
                    originUri.getScheme(), originUri.getAuthority(), originUri.getPath(),
                    route.getId(), url.getScheme(), url.getAuthority(), url.getPath(), LocalDateTime.now()
            );
        }
    }

    /**
     * 给请求头添加防护token
     *
     * @param exchange
     * @return
     */
    private ServerWebExchange handleServerWebExchange(ServerWebExchange exchange) {
        // 网关防护token
        byte[] gatewayToken = Base64Utils.encode(CommonConstant.CLOUD_GATEWAY_TOKEN_VALUE.getBytes());
        // 设置网关防护token到请求头
        ServerHttpRequest mutableReq = exchange.getRequest().mutate()
                .header(CommonConstant.CLOUD_GATEWAY_TOKEN_HEADER, new String(gatewayToken))
                .build();
        return exchange.mutate().request(mutableReq).build();
    }
}
