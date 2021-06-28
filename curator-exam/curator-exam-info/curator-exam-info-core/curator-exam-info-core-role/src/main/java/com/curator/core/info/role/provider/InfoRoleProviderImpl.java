package com.curator.core.info.role.provider;

import com.curator.api.info.provider.InfoRoleProvider;
import com.curator.common.support.ResultResponse;
import com.curator.core.info.role.service.RoleService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 角色信息 RPC接口 实现
 *
 * @author Jun
 * @date 2021/4/19
 */
@DubboService
public class InfoRoleProviderImpl implements InfoRoleProvider {

    @Autowired
    private RoleService roleService;

    @Override
    public ResultResponse<String> getRoleName(String roleId) {
        return roleService.getRoleName(roleId);
    }
}
