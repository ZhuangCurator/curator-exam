package com.curator.backend.info.menu.entity.vo.info;

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
public class InfoMenuInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单主键
     */
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

}
