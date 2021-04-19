package com.curator.common.constant;

/**
 * 基础常量
 *
 * @author Jun
 * @date 2021/4/19
 */
public class CommonConstant {

    /**
     * 令牌 有效期(分钟)
     */
    public static final long TOKEN_EXPIRE_TIME = 60;

    /**
     * 令牌自定义标识
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 登录账户缓存前缀
     */
    public static final String CACHE_ACCOUNT_PREFIX = "cache:account:";

    /**
     * 默认的超级管理员角色名
     */
    public static final String DEFAULT_SUPER_ADMIN_ROLE = "SUPER_ADMIN_ROLE";
}
