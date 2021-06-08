package com.curator.backend.info.power.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.curator.api.info.enums.InfoPowerStatusEnum;
import com.curator.api.info.enums.InfoPowerTypeEnum;
import com.curator.common.util.Help;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
     * 权限类型描述
     */
    private String powerTypeDesc;

    /**
     * 权限状态(1启用 2停用)
     */
    private Integer powerStatus;

    /**
     * 权限状态描述
     */
    private String powerStatusDesc;

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
     * 是否隐藏
     */
    private Integer hide;

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

    /**
     * 子权限
     */
    private List<InfoPowerDTO> children;

    public String getPowerTypeDesc() {
        if(Help.isNotEmpty(powerType)) {
            return InfoPowerTypeEnum.getDesc(powerType);
        }
        return null;
    }

    public String getPowerStatusDesc() {
        if(Help.isNotEmpty(powerStatus)) {
            return InfoPowerStatusEnum.getDesc(powerStatus);
        }
        return null;
    }
}
