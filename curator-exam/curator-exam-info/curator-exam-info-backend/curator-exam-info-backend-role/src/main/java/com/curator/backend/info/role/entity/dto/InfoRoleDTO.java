package com.curator.backend.info.role.entity.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.curator.api.info.enums.InfoRoleStatusEnum;
import com.curator.api.info.enums.InfoRoleTypeEnum;
import com.curator.common.util.Help;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 角色信息 数据传输对象
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InfoRoleDTO implements Serializable {

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
     * 角色状态描述
     */
    private String roleStatusDesc;

    /**
     * 角色类型 1:超级管理员,2:超级管理员子账号,3:省级管理员,4:省级管理员子账号,5:市级管理员,6:市级管理员子账号
     */
    private Integer roleType;

    /**
     * 角色类型描述
     */
    private String roleTypeDesc;

    /**
     * 备注
     */
    private String roleRemark;

    /**
     * 创建账户 id
     */
    private String createAccountId;

    /**
     * 创建账户名称
     */
    private String createAccountName;

    /**
     * 创建账户父账户 id
     */
    private String parentAccountId;

    /**
     * 1 表示删除，0 表示未删除
     */
    private Integer deleted;

    /**
     * 插入时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 已绑定的权限组ID集合
     */
    private List<String> powerGroupIdList;

    public String getRoleStatusDesc() {
        if(Help.isNotEmpty(roleStatus)) {
            return InfoRoleStatusEnum.getDesc(roleStatus);
        }
        return null;
    }

    public String getRoleTypeDesc() {
        if(Help.isNotEmpty(roleType)) {
            return InfoRoleTypeEnum.getDesc(roleType);
        }
        return null;
    }
}
