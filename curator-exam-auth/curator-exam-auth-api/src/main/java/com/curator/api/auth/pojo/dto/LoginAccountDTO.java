package com.curator.api.auth.pojo.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Jun
 * @date 2021/4/19
 */
@Data
@Builder
public class LoginAccountDTO implements Serializable {

    /**
     * 账户ID
     */
    private String accountId;

    /**
     * 父账户ID
     */
    private String parentAccountId;

    /**
     * 账户名
     */
    private String accountName;

    /**
     * 登录唯一标识
     */
    private String token;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 权限列表
     */
    private Set<String> perms;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;
}
