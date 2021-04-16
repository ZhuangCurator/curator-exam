package com.curator.common.support;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询基础类
 *
 * @author Jun
 * @date 2021/4/15
 */
@Data
public class BaseSearch implements Serializable {

    /**
     * 当前页
     */
    private Integer current = 1;

    /**
     * 每页展示数据量
     */
    private Integer pageSize = 20;

    /**
     * 是否是超级管理员
     */
    private Boolean superAdmin = Boolean.FALSE;

    /**
     * 创建账户ID
     */
    private String createAccountId;

}
