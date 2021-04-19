package com.curator.core.info.role.service;

import com.curator.common.support.ResultResponse;

/**
 * @author Jun
 * @date 2021/4/19
 */
public interface RoleService {

    /**
     * 根据id获取角色名
     *
     * @param roleId 角色id
     * @return 角色名
     */
    ResultResponse<String> getRoleName(String roleId);
}
