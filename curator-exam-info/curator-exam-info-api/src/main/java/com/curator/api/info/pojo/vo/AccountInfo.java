package com.curator.api.info.pojo.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 账户 页面信息
 *
 * @author Jun
 * @date 2021/5/18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AccountInfo implements Serializable {

    /**
     * 账户主键
     */
    private String accountId;

    /**
     * 账户名称
     */
    private String accountName;

    /**
     * 考生身份证号
     */
    private String idCard;

    /**
     * 账户密码
     */
    private String accountPassword;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;
}
