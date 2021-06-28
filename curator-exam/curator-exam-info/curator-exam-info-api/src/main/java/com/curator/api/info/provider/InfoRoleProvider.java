package com.curator.api.info.provider;

import com.curator.common.support.ResultResponse;

/**
 *
 * 角色信息 RPC接口
 *
 * @author Jun
 * @date 2021/4/19
 */
public interface InfoRoleProvider {

    /**
     * 获取角色名称
     *
     * @param roleId 角色id
     * @return 角色名称
     */
    ResultResponse<String> getRoleName(String roleId);
}
