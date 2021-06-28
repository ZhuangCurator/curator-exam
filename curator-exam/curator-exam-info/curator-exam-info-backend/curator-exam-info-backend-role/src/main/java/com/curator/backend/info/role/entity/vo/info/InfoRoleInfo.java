package com.curator.backend.info.role.entity.vo.info;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 角色信息 页面信息
 *
 * @author Jun
 * @since 2021-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InfoRoleInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色主键
     */
    private String roleId;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 角色状态（1-启用，2-停用）
     */
    private Integer roleStatus;

    /**
     * 角色类型 1:超级管理员,2:超级管理员子账号,3:省级管理员,4:省级管理员子账号,5:市级管理员,6:市级管理员子账号
     */
    private Integer roleType;

    /**
     * 备注
     */
    private String roleRemark;

}
