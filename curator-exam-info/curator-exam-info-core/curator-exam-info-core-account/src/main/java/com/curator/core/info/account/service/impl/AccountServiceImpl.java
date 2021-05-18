package com.curator.core.info.account.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.curator.api.info.enums.InfoAccountStatusEnum;
import com.curator.api.info.pojo.dto.AccountDTO;
import com.curator.api.info.pojo.vo.AccountInfo;
import com.curator.common.constant.CommonConstant;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.common.util.SecurityUtil;
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
import org.springframework.beans.BeanUtils;
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
        if (Help.isNotEmpty(infoRole)) {
            if (CommonConstant.DEFAULT_SUPER_ADMIN_ROLE.equals(infoRole.getRoleName())) {
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

    @Override
    public ResultResponse<AccountDTO> saveInfoAccount(AccountInfo info) {
        InfoAccount entity = convertInfo(info);
        // 判断账户名是否重复
        QueryWrapper<InfoAccount> wrapper = new QueryWrapper<>();
        wrapper.eq("account_name", info.getAccountName());
        InfoAccount infoAccount = accountMapper.selectOne(wrapper);
        if (Help.isNotEmpty(infoAccount)) {
            // 数据库已存在该账户名
            return ResultResponse.<AccountDTO>builder().failure("该账户名已被使用!").build();
        }
        // 判断手机号码是否重复
        wrapper = new QueryWrapper<>();
        wrapper.eq("phone", info.getPhone());
        infoAccount = accountMapper.selectOne(wrapper);
        if (Help.isNotEmpty(infoAccount)) {
            // 数据库已存在该手机号码
            return ResultResponse.<AccountDTO>builder().failure("该手机号已被绑定!").build();
        }
        String salt = RandomUtil.randomString(RandomUtil.randomInt(10, 15));
        // 普通账户密码默认为 123456
        String encryptPassword = SecurityUtil.encryptPassword("123456", salt);
        entity.setAccountPassword(encryptPassword);
        entity.setSalt(salt);
        // 新账户状态为正常
        entity.setAccountStatus(InfoAccountStatusEnum.NORMAL.getStatus());
        accountMapper.insert(entity);
        return ResultResponse.<AccountDTO>builder().success("账户添加成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<?> updateAccount(AccountInfo info) {
        if (Help.isEmpty(info.getAccountPassword()) && Help.isEmpty(info.getPhone()) && Help.isEmpty(info.getEmail())) {
            return ResultResponse.builder().failure("请填写需要修改的账户信息!").build();
        }
        InfoAccount entity = new InfoAccount();
        if (Help.isNotEmpty(info.getAccountPassword())) {
            String salt = RandomUtil.randomString(RandomUtil.randomInt(10, 15));
            String encryptPassword = SecurityUtil.encryptPassword(info.getAccountPassword(), salt);
            entity.setAccountPassword(encryptPassword);
        }
        entity.setEmail(info.getEmail());
        entity.setPhone(info.getPhone());
        accountMapper.updateById(entity);
        return ResultResponse.builder().success("账户信息修改成功!").build();
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

    /**
     * 将 {@link InfoAccount} 转为 {@link AccountDTO}
     *
     * @param entity 账户数据库信息
     * @return {@link AccountDTO}
     */
    private AccountDTO convertEntity(InfoAccount entity) {
        AccountDTO target = new AccountDTO();
        if (Help.isNotEmpty(entity)) {
            BeanUtils.copyProperties(entity, target);
        }
        return target;
    }

    /**
     * 将 {@link AccountInfo} 转为 {@link InfoAccount}
     *
     * @param info 账户 页面信息
     * @return {@link InfoAccount}
     */
    private InfoAccount convertInfo(AccountInfo info) {
        InfoAccount target = new InfoAccount();
        if (Help.isNotEmpty(info)) {
            BeanUtils.copyProperties(info, target);
        }
        return target;
    }
}
