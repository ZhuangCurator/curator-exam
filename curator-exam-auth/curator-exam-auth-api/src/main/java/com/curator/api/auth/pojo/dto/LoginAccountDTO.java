package com.curator.api.auth.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author Jun
 * @date 2021/4/19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginAccountDTO implements Serializable {

    /**
     * 账户ID
     */
    private String accountId;

    /**
     * 该账户的父账户ID
     */
    private String parentAccountId;

    /**
     * 该账户的所有层级的下级账户ID
     */
    private List<String> childrenAccountIdList;

    /**
     * 账户名
     */
    private String accountName;

    /**
     * 省(代码)
     */
    private String province;

    /**
     * 市(代码)
     */
    private String city;

    /**
     * 登录唯一标识
     */
    private String token;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 角色类型
     */
    private Integer roleType;

    /**
     * 权限列表
     */
    private Set<String> perms;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间(毫秒)
     */
    private Long expireTime;
}
