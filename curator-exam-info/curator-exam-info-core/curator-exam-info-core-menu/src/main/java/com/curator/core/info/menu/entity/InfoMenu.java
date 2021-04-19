package com.curator.core.info.menu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 菜单信息
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InfoMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单主键
     */
    @TableId(value = "menu_id", type = IdType.ASSIGN_ID)
    private String menuId;

    /**
     * 菜单名
     */
    private String menuName;

    /**
     * 父菜单id
     */
    private String parentId;

    /**
     * 菜单顺序
     */
    private Integer menuOrder;

    /**
     * 菜单类型(1菜单 2按钮 3 目录)
     */
    private Integer menuType;

    /**
     * 菜单状态(1启用 2停用)
     */
    private Integer menuStatus;

    /**
     * 路由地址
     */
    private String menuPath;

    /**
     * 组件路径
     */
    private String menuComponent;

    /**
     * 权限标识
     */
    private String menuPerms;

    /**
     * 菜单图标
     */
    private String menuIcon;

    /**
     * 备注
     */
    private String menuRemark;

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
