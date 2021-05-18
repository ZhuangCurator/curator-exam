package com.curator.api.info.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 账户 数据传输对象
 *
 * @author Jun
 * @date 2021/5/18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AccountDTO implements Serializable {

    /**
     * 账户主键
     */
    private String accountId;

    /**
     * 账户名称
     */
    private String accountName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;
}
