package com.curator.core.info.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.curator.common.constant.CommonConstant;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.core.info.account.entity.InfoAccount;
import com.curator.core.info.account.entity.InfoAccountPower;
import com.curator.core.info.account.mapper.InfoAccountMapper;
import com.curator.core.info.account.mapper.InfoAccountPowerMapper;
import com.curator.core.info.account.service.AccountService;
import com.curator.core.info.power.entity.InfoGroupPower;
import com.curator.core.info.power.entity.InfoPower;
import com.curator.core.info.power.mapper.InfoGroupPowerMapper;
import com.curator.core.info.power.mapper.InfoPowerMapper;
import com.curator.core.info.role.entity.InfoRole;
import com.curator.core.info.role.entity.InfoRolePowerGroup;
import com.curator.core.info.role.mapper.InfoRoleMapper;
import com.curator.core.info.role.mapper.InfoRolePowerGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Jun
 * @date 2021/4/19
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private InfoAccountMapper accountMapper;
    @Autowired
    private InfoRoleMapper roleMapper;
    @Autowired
    private InfoRolePowerGroupMapper rolePowerGroupMapper;
    @Autowired
    private InfoGroupPowerMapper groupPowerMapper;
    @Autowired
    private InfoAccountPowerMapper accountPowerMapper;
    @Autowired
    private InfoPowerMapper powerMapper;

    @Override
    public ResultResponse<Set<String>> getAccountAllPerms(String accountId) {
        Set<String> permsSet = new HashSet<>();
        InfoAccount account = accountMapper.selectById(accountId);
        InfoRole infoRole = roleMapper.selectById(account.getRoleId());
        if(Help.isNotEmpty(infoRole)) {
            if(CommonConstant.DEFAULT_SUPER_ADMIN_ROLE.equals(infoRole.getRoleName())) {
                // 是超级管理员
                permsSet.add("*:*:*");
            } else {
                // 查询账户角色对应的权限标识
                QueryWrapper<InfoRolePowerGroup> rolePowerGroupWrapper = new QueryWrapper<>();
                rolePowerGroupWrapper.eq("role_id", account.getRoleId());
                List<InfoRolePowerGroup> rolePowerGroupList = rolePowerGroupMapper.selectList(rolePowerGroupWrapper);
                if (Help.isNotEmpty(rolePowerGroupList)) {
                    List<String> powerGroupIdList = rolePowerGroupList.stream()
                            .map(InfoRolePowerGroup::getPowerGroupId).collect(Collectors.toList());
                    QueryWrapper<InfoGroupPower> groupPowerWrapper = new QueryWrapper<>();
                    groupPowerWrapper.in("power_group_id", powerGroupIdList);
                    List<InfoGroupPower> groupPowerList = groupPowerMapper.selectList(groupPowerWrapper);
                    if (Help.isNotEmpty(groupPowerList)) {
                        List<String> powerIdList = groupPowerList.stream()
                                .map(InfoGroupPower::getPowerId).collect(Collectors.toList());
                        if (Help.isNotEmpty(powerIdList)) {
                            Set<String> permSetWithRole = getPowerPermsSet(powerIdList);
                            if (Help.isNotEmpty(permSetWithRole)) {
                                permsSet.addAll(permSetWithRole);
                            }
                        }
                    }
                }
            }
        }
        // 查询账户对应的权限标识
        QueryWrapper<InfoAccountPower> accountPowerWrapper = new QueryWrapper<>();
        accountPowerWrapper.eq("account_id", account.getAccountId());
        List<InfoAccountPower> accountPowerList = accountPowerMapper.selectList(accountPowerWrapper);
        if (Help.isNotEmpty(accountPowerList)) {
            List<String> powerIdList = accountPowerList.stream().map(InfoAccountPower::getPowerId).collect(Collectors.toList());
            Set<String> set = getPowerPermsSet(powerIdList);
            if (Help.isNotEmpty(set)) {
                permsSet.addAll(set);
            }
        }
        return ResultResponse.<Set<String>>builder().success("账户权限标识查询成功").data(permsSet).build();
    }

    /**
     * 查询权限集合的权限
     *
     * @param powerIdList 权限id集合
     * @return
     */
    private Set<String> getPowerPermsSet(List<String> powerIdList) {
        QueryWrapper<InfoPower> powerQueryWrapper = new QueryWrapper<>();
        powerQueryWrapper.in("power_id", powerIdList);
        List<InfoPower> powerList = powerMapper.selectList(powerQueryWrapper);
        if (Help.isNotEmpty(powerList)) {
            return powerList.stream().map(InfoPower::getPowerPerms).collect(Collectors.toSet());
        }
        return null;
    }
}
