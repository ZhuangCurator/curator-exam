package com.curator.core.info.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.curator.common.constant.CommonConstant;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.core.info.account.entity.InfoAccount;
import com.curator.core.info.account.entity.InfoAccountMenu;
import com.curator.core.info.account.mapper.InfoAccountMapper;
import com.curator.core.info.account.mapper.InfoAccountMenuMapper;
import com.curator.core.info.account.service.AccountService;
import com.curator.core.info.menu.entity.InfoGroupMenu;
import com.curator.core.info.menu.entity.InfoMenu;
import com.curator.core.info.menu.mapper.InfoGroupMenuMapper;
import com.curator.core.info.menu.mapper.InfoMenuMapper;
import com.curator.core.info.role.entity.InfoRole;
import com.curator.core.info.role.entity.InfoRoleMenuGroup;
import com.curator.core.info.role.mapper.InfoRoleMapper;
import com.curator.core.info.role.mapper.InfoRoleMenuGroupMapper;
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
    private InfoRoleMenuGroupMapper roleMenuGroupMapper;
    @Autowired
    private InfoGroupMenuMapper groupMenuMapper;
    @Autowired
    private InfoAccountMenuMapper accountMenuMapper;
    @Autowired
    private InfoMenuMapper menuMapper;

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
                QueryWrapper<InfoRoleMenuGroup> roleMenuGroupWrapper = new QueryWrapper<>();
                roleMenuGroupWrapper.eq("role_id", account.getRoleId());
                List<InfoRoleMenuGroup> roleMenuGroupList = roleMenuGroupMapper.selectList(roleMenuGroupWrapper);
                if (Help.isNotEmpty(roleMenuGroupList)) {
                    List<String> menuGroupIdList = roleMenuGroupList.stream()
                            .map(InfoRoleMenuGroup::getMenuGroupId).collect(Collectors.toList());
                    QueryWrapper<InfoGroupMenu> groupMenuWrapper = new QueryWrapper<>();
                    groupMenuWrapper.in("menu_group_id", menuGroupIdList);
                    List<InfoGroupMenu> groupMenuList = groupMenuMapper.selectList(groupMenuWrapper);
                    if (Help.isNotEmpty(groupMenuList)) {
                        List<String> menuIdList = groupMenuList.stream()
                                .map(InfoGroupMenu::getMenuId).collect(Collectors.toList());
                        if (Help.isNotEmpty(menuIdList)) {
                            Set<String> permSetWithRole = getMenuPermsSet(menuIdList);
                            if (Help.isNotEmpty(permSetWithRole)) {
                                permsSet.addAll(permSetWithRole);
                            }
                        }
                    }
                }
            }
        }
        // 查询账户对应的权限标识
        QueryWrapper<InfoAccountMenu> accountMenuWrapper = new QueryWrapper<>();
        accountMenuWrapper.eq("account_id", account.getAccountId());
        List<InfoAccountMenu> accountMenuList = accountMenuMapper.selectList(accountMenuWrapper);
        if (Help.isNotEmpty(accountMenuList)) {
            List<String> menuIdList = accountMenuList.stream().map(InfoAccountMenu::getMenuId).collect(Collectors.toList());
            Set<String> set = getMenuPermsSet(menuIdList);
            if (Help.isNotEmpty(set)) {
                permsSet.addAll(set);
            }
        }
        return ResultResponse.<Set<String>>builder().success("账户权限标识查询成功").data(permsSet).build();
    }

    /**
     * 查询菜单集合的权限
     *
     * @param menuIdList 菜单id集合
     * @return
     */
    private Set<String> getMenuPermsSet(List<String> menuIdList) {
        QueryWrapper<InfoMenu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.in("menu_id", menuIdList);
        List<InfoMenu> menuList = menuMapper.selectList(menuQueryWrapper);
        if (Help.isNotEmpty(menuList)) {
            return menuList.stream().map(InfoMenu::getMenuPerms).collect(Collectors.toSet());
        }
        return null;
    }
}
