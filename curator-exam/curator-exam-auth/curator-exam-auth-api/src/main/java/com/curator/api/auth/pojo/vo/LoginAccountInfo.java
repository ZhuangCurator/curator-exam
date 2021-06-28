package com.curator.api.auth.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Jun
 * @date 2021/4/18
 */
@Data
public class LoginAccountInfo implements Serializable {

    /**
     * 账户名称
     */
    private String accountName;

    /**
     * 账户密码
     */
    private String accountPassword;

}
