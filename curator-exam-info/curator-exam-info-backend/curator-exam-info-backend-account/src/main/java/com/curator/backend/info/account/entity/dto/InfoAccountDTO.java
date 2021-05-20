package com.curator.backend.info.account.entity.dto;

import com.curator.api.info.enums.InfoAccountStatusEnum;
import com.curator.api.info.enums.InfoRoleTypeEnum;
import com.curator.common.util.Help;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 账户信息 数据传输信息
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InfoAccountDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账户主键
     */
    private String accountId;

    /**
     * 账户名
     */
    private String accountName;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 账户状态（1:正常，2:冻结，3:注销）
     */
    private Integer accountStatus;

    /**
     * 账户状态描述
     */
    private String accountStatusDesc;

    /**
     * 账户角色
     */
    private String roleId;

    /**
     * 角色类型
     */
    private Integer roleType;

    /**
     * 角色类型描述
     */
    private String roleTypeDesc;

    /**
     * 省(代码)
     */
    private String province;

    /**
     * 市(代码)
     */
    private String city;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 父账号id
     */
    private String parentId;

    /**
     * 备注
     */
    private String remark;

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

    public String getAccountStatusDesc() {
        return InfoAccountStatusEnum.getDesc(accountStatus);
    }

    public String getRoleTypeDesc() {
        return Help.isEmpty(roleType) ? "" : InfoRoleTypeEnum.getDesc(roleType);
    }
}
