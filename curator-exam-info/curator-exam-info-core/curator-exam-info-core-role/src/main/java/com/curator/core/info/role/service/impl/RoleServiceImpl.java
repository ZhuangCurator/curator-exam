package com.curator.core.info.role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.core.info.role.entity.InfoRole;
import com.curator.core.info.role.mapper.InfoRoleMapper;
import com.curator.core.info.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jun
 * @date 2021/4/19
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private InfoRoleMapper roleMapper;

    @Override
    public ResultResponse<String> getRoleName(String roleId) {
        InfoRole infoRole = roleMapper.selectById(roleId);
        if(Help.isNotEmpty(infoRole)) {
            return ResultResponse.<String>builder().success("角色名查询成功").data(infoRole.getRoleName()).build();
        }
        return ResultResponse.<String>builder().failure("角色不存在").build();
    }
}
