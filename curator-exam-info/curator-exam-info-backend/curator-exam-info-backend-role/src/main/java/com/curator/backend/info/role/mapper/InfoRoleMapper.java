package com.curator.backend.info.role.mapper;

import com.curator.backend.info.role.entity.InfoRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  角色信息 Mapper 接口
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
public interface InfoRoleMapper extends BaseMapper<InfoRole> {

    /**
     * 查询账号的角色类型
     *
     * @param accountId 账号ID
     * @return
     */
    Integer getAccountRoleType(String accountId);

    /**
     * 获取角色绑定的权限组ID列表
     *
     * @param roleId 角色ID
     * @return
     */
    List<String> getPowerGroupIdWithRole(String roleId);
}
