package com.curator.backend.info.power.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 权限信息 数据传输信息
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InfoPowerDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限主键
     */
    private String powerId;

    /**
     * 权限名
     */
    private String powerName;

    /**
     * 父权限id
     */
    private String parentId;

    /**
     * 权限顺序
     */
    private Integer powerOrder;

    /**
     * 权限类型(1权限 2按钮 3 目录)
     */
    private Integer powerType;

    /**
     * 权限状态(1启用 2停用)
     */
    private Integer powerStatus;

    /**
     * 路由地址
     */
    private String powerPath;

    /**
     * 组件路径
     */
    private String powerComponent;

    /**
     * 权限标识
     */
    private String powerPerms;

    /**
     * 权限图标
     */
    private String powerIcon;

    /**
     * 备注
     */
    private String powerRemark;

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
    private Integer deleted;

    /**
     * 插入时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
