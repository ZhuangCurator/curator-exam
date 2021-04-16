package com.curator.backend.info.account.entity.vo.info;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 账户信息 页面信息
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InfoAccountInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账户主键
     */
    private String accountId;

    /**
     * 账户名
     */
    private String accountName;

    /**
     * 账户密码
     */
    private String accountPassword;

    /**
     * 加密盐
     */
    private String salt;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 账户状态（1:正常，2:冻结，3:注销）
     */
    private Integer accountStatus;

    /**
     * 账户角色
     */
    private String roleId;

    /**
     * 父账号id
     */
    private String parentId;

    /**
     * 创建账户 id
     */
    private String createAccountId;

    /**
     * 创建账户父账户 id
     */
    private String parentAccountId;

}
