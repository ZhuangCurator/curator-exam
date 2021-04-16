package com.curator.backend.info.role.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色与菜单组关联
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InfoRoleMenuGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "role_menu_id", type = IdType.ASSIGN_ID)
    private String roleMenuId;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 菜单组id
     */
    private String menuGroupId;

    /**
     * 创建账户 id
     */
    private String createAccountId;

    /**
     * 创建账户父账户 id
     */
    private String parentAccountId;

    /**
     * 1 表示删除，0 表示未删除
     */
    @TableLogic
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    private Integer deleted;

    /**
     * 插入时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
