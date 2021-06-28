package com.curator.common.constant;

/**
 * 基础常量
 *
 * @author Jun
 * @date 2021/4/19
 */
public class CommonConstant {

    /**
     * 验证码 缓存前缀
     */
    public static final String CAPTCHA_CACHE_KEY = "captcha:cache:";

    /**
     * 令牌 有效期(毫秒)
     */
    public static final long TOKEN_EXPIRE_TIME = 8 * 60 * 60 * 1000;

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

    /**
     * 默认的超级管理员名
     */
    public static final String DEFAULT_SUPER_ADMIN = "SUPER_ADMIN";

    /**
     * 防护网关请求头TOKEN名称
     */
    public static final String CLOUD_GATEWAY_TOKEN_HEADER = "CURATOR_GATEWAY_TOKEN";

    /**
     * 防护网关请求头TOKEN值
     */
    public static final String CLOUD_GATEWAY_TOKEN_VALUE = "CURATOR:GATEWAY:T9KaDB3FLPxRnX";

    /**
     * 账户id标识
     */
    public static final String HTTP_HEADER_ACCOUNT_ID = "accountId";

    /**
     * 父级账户id标识
     */
    public static final String HTTP_HEADER_ACCOUNT_PARENT_ID = "parentAccountId";

    /**
     * 下级账户id标识
     */
    public static final String HTTP_HEADER_ACCOUNT_CHILDREN_ID = "childrenAccountIdList";

    /**
     * 账户名标识
     */
    public static final String HTTP_HEADER_ACCOUNT_NAME = "accountName";

    /**
     * 账户省份
     */
    public static final String HTTP_HEADER_ACCOUNT_PROVINCE = "province";

    /**
     * 账户地市
     */
    public static final String HTTP_HEADER_ACCOUNT_CITY = "city";

    /**
     * 前后分离时 发送到前端的验证码图片经过base64加密后 加上此前缀
     */
    public static final String BASE64_IMAGE_PREFIX = "data:image/jpeg;base64,";
}
