package com.curator.backend.info.role.entity.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.curator.api.info.enums.InfoRoleStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

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
     * 备注
     */
    private String roleRemark;

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

    public String getRoleStatusDesc() {
        return InfoRoleStatusEnum.getDesc(roleStatus);
    }
}
