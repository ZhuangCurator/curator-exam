package com.curator.backend.info.power.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.curator.api.info.enums.InfoPowerStatusEnum;
import com.curator.api.info.enums.InfoPowerTypeEnum;
import com.curator.backend.info.account.entity.InfoAccount;
import com.curator.backend.info.account.mapper.InfoAccountMapper;
import com.curator.backend.info.power.entity.InfoAccountPower;
import com.curator.backend.info.power.entity.InfoGroupPower;
import com.curator.backend.info.power.entity.InfoPower;
import com.curator.backend.info.power.entity.InfoRolePowerGroup;
import com.curator.backend.info.power.entity.dto.InfoPowerDTO;
import com.curator.backend.info.power.entity.dto.RouterDTO;
import com.curator.backend.info.power.entity.vo.info.InfoPowerInfo;
import com.curator.backend.info.power.entity.vo.search.InfoPowerSearch;
import com.curator.backend.info.power.mapper.InfoAccountPowerMapper;
import com.curator.backend.info.power.mapper.InfoGroupPowerMapper;
import com.curator.backend.info.power.mapper.InfoPowerMapper;
import com.curator.backend.info.power.mapper.InfoRolePowerGroupMapper;
import com.curator.backend.info.power.service.InfoPowerService;
import com.curator.backend.info.role.entity.InfoRole;
import com.curator.backend.info.role.mapper.InfoRoleMapper;
import com.curator.common.constant.CommonConstant;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.common.util.ServletUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限信息 服务实现类
 *
 * @author Jun
 * @date 2021/4/17
 */
@Service
public class InfoPowerServiceImpl implements InfoPowerService {

    @Autowired
    private InfoPowerMapper powerMapper;
    @Autowired
    private InfoAccountMapper accountMapper;
    @Autowired
    private InfoRoleMapper roleMapper;
    @Autowired
    private InfoAccountPowerMapper accountPowerMapper;
    @Autowired
    private InfoRolePowerGroupMapper rolePowerGroupMapper;
    @Autowired
    private InfoGroupPowerMapper groupPowerMapper;

    @Override
    public ResultResponse<List<RouterDTO>> selectRouter() {
        // 账户ID
        String accountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
        // 账户
        InfoAccount infoAccount = accountMapper.selectById(accountId);
        // 账户角色
        InfoRole infoRole = roleMapper.selectById(infoAccount.getRoleId());
        Set<String> powerSet = new HashSet<>();
        // 如果不是超级管理员
        if (!infoRole.getRoleName().equals(CommonConstant.DEFAULT_SUPER_ADMIN_ROLE)) {
            // 查询账户本身具有的权限
            QueryWrapper<InfoAccountPower> accountPowerWrapper = new QueryWrapper<>();
            accountPowerWrapper.eq("account_id", accountId);
            List<InfoAccountPower> accountPowerList = accountPowerMapper.selectList(accountPowerWrapper);
            if (Help.isNotEmpty(accountPowerList)) {
                powerSet.addAll(accountPowerList.parallelStream().map(InfoAccountPower::getPowerId).collect(Collectors.toSet()));
            }
            // 查询账户角色对应的权限组所包含的权限
            QueryWrapper<InfoRolePowerGroup> rolePowerGroupWrapper = new QueryWrapper<>();
            rolePowerGroupWrapper.eq("role_id", infoAccount.getRoleId());
            List<InfoRolePowerGroup> rolePowerGroupList = rolePowerGroupMapper.selectList(rolePowerGroupWrapper);
            if (Help.isNotEmpty(rolePowerGroupList)) {
                // 得到权限组ID集合
                List<String> powerGroupIdList = rolePowerGroupList.stream().map(InfoRolePowerGroup::getPowerGroupId)
                        .collect(Collectors.toList());
                // 查询权限
                QueryWrapper<InfoGroupPower> groupPowerWrapper = new QueryWrapper<>();
                groupPowerWrapper.in("power_group_id", powerGroupIdList);
                List<InfoGroupPower> groupPowerList = groupPowerMapper.selectList(groupPowerWrapper);
                if (Help.isNotEmpty(groupPowerList)) {
                    powerSet.addAll(groupPowerList.parallelStream().map(InfoGroupPower::getPowerId).collect(Collectors.toSet()));
                }
            }
            if (Help.isEmpty(powerSet)) {
                // 非超级管理员同时没有任何权限
                return ResultResponse.<List<RouterDTO>>builder().failure("该账户没有权限访问系统!").build();
            }
        }
        // 查询类型是目录和菜单的权限
        QueryWrapper<InfoPower> powerWrapper = new QueryWrapper<>();
        powerWrapper.eq("power_status", InfoPowerStatusEnum.ENABLE.getStatus())
                .in(Help.isNotEmpty(powerSet), "power_id", powerSet)
                .and(w -> w.eq("power_type", InfoPowerTypeEnum.MENU.getStatus())
                        .or(wr -> wr.eq("power_type", InfoPowerTypeEnum.DIR.getStatus())));
        List<InfoPower> infoPowerList = powerMapper.selectList(powerWrapper);
        Set<InfoPowerDTO> infoPowerSet = infoPowerList.stream().map(this::convertEntity).collect(Collectors.toSet());
        // 首选获取第一级权限
        String parentId = "0";
        List<InfoPowerDTO> treeList = infoPowerSet.parallelStream().filter(power -> power.getParentId().equals(parentId)).sorted(Comparator.comparing(InfoPowerDTO::getPowerOrder)).collect(Collectors.toList());
        // 递归获取子权限
        treeList.forEach(parent -> selectPowerChild(parent, infoPowerSet));
        // 获取路由集合
        List<RouterDTO> routerDTOList = buildRouterTree(treeList);
        return ResultResponse.<List<RouterDTO>>builder().success("路由集合查询成功!").data(routerDTOList).build();
    }

    @Override
    public ResultResponse<List<InfoPowerDTO>> treeWithInfoPower(InfoPowerSearch search) {
        // 树状结构
        List<InfoPowerDTO> result = new ArrayList<>();
        QueryWrapper<InfoPower> wrapper = new QueryWrapper<>();
        wrapper.like(Help.isNotEmpty(search.getPowerName()), "power_name", search.getPowerName())
                .eq(Help.isNotEmpty(search.getPowerType()), "power_type", search.getPowerType())
                .eq(Help.isNotEmpty(search.getPowerStatus()), "power_status", search.getPowerStatus())
                .orderByDesc("create_time");
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(w -> w.eq("parent_account_id", createAccountId)));
        }
        List<InfoPower> list = powerMapper.selectList(wrapper);
        if (Help.isNotEmpty(list)) {
            Set<InfoPowerDTO> resultSet = list.stream().map(this::convertEntity).collect(Collectors.toSet());
            if (Help.isNotEmpty(search.getPowerName()) || Help.isNotEmpty(search.getPowerType()) || Help.isNotEmpty(search.getPowerStatus())) {
                // 若是进行了条件过滤, 那么查出这些权限的父级权限
                Set<InfoPowerDTO> parentSet = new HashSet<>();
                if (Help.isNotEmpty(resultSet)) {
                    resultSet.parallelStream().forEach(resultDTO -> selectPowerParent(parentSet, resultDTO));
                }
                resultSet.addAll(parentSet);
            }
            // 首选获取第一级权限
            String parentId = "0";
            result = resultSet.parallelStream().filter(power -> power.getParentId().equals(parentId)).sorted(Comparator.comparing(InfoPowerDTO::getPowerOrder)).collect(Collectors.toList());
            // 递归获取子权限
            result.forEach(parent -> selectPowerChild(parent, resultSet));
        }
        return ResultResponse.<List<InfoPowerDTO>>builder().success("权限树状列表查询成功").data(result).build();
    }

    @Override
    public ResultResponse<InfoPowerDTO> getInfoPower(String infoPowerId) {
        InfoPower entity = powerMapper.selectById(infoPowerId);
        return ResultResponse.<InfoPowerDTO>builder().success("权限查询成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<InfoPowerDTO> saveInfoPower(InfoPowerInfo info) {
        InfoPower entity = convertInfo(info);
        // 默认启用
        entity.setPowerStatus(InfoPowerStatusEnum.ENABLE.getStatus());
        entity.setCreateAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID));
        entity.setParentAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_PARENT_ID));
        powerMapper.insert(entity);
        return ResultResponse.<InfoPowerDTO>builder().success("权限添加成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<InfoPowerDTO> putInfoPower(InfoPowerInfo info) {
        InfoPower entity = convertInfo(info);
        powerMapper.update(entity, new UpdateWrapper<InfoPower>().eq("power_id", info.getPowerId()));
        return ResultResponse.<InfoPowerDTO>builder().success("权限更新成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<String> removeInfoPower(String infoPowerId) {
        powerMapper.deleteById(infoPowerId);
        // 同时删除与权限组的关联关系
        QueryWrapper<InfoGroupPower> wrapper = new QueryWrapper<>();
        wrapper.eq("power_id", infoPowerId);
        groupPowerMapper.delete(wrapper);
        // 同时删除与账户的关联关系
        QueryWrapper<InfoAccountPower> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("power_id", infoPowerId);
        accountPowerMapper.delete(queryWrapper);
        return ResultResponse.<String>builder().success("权限删除成功").data(infoPowerId).build();
    }

    /**
     * 构建路由树
     *
     * @param powerDTOList 权限集合
     * @return
     */
    private List<RouterDTO> buildRouterTree(List<InfoPowerDTO> powerDTOList) {
        List<RouterDTO> routers = new LinkedList<>();
        for (InfoPowerDTO powerDTO : powerDTOList) {
            RouterDTO routerDTO = new RouterDTO();
            routerDTO.setName(getRouterName(powerDTO.getPowerPath()));
            routerDTO.setPath(getRouterPath(powerDTO));
            routerDTO.setComponent(getRouterComponent(powerDTO));
            routerDTO.setMeta(new RouterDTO.Meta(powerDTO.getPowerName(), powerDTO.getPowerIcon()));
            List<InfoPowerDTO> childrenList = powerDTO.getChildren();
            if (Help.isNotEmpty(childrenList)) {
                routerDTO.setChildren(buildRouterTree(childrenList));
            }
            routers.add(routerDTO);
        }
        return routers;
    }

    /**
     * 修饰路由路径
     *
     * @param powerDTO 权限对象
     * @return
     */
    private String getRouterPath(InfoPowerDTO powerDTO) {
        String path = powerDTO.getPowerPath();
        if (powerDTO.getPowerType().equals(InfoPowerTypeEnum.DIR.getStatus())) {
            path = "/" + path;
        }
        return path;
    }

    /**
     * 修饰路由名称 以路由的路径首字母大写
     *
     * @param name
     * @return
     */
    private String getRouterName(String name) {
        if (Character.isUpperCase(name.charAt(0))) {
            return name;
        } else {
            return Character.toUpperCase(name.charAt(0)) + name.substring(1);
        }
    }

    /**
     * 修饰路由组件
     *
     * @param powerDTO
     * @return
     */
    private String getRouterComponent(InfoPowerDTO powerDTO) {
        String component = "home";
        if (Help.isNotEmpty(powerDTO.getPowerComponent())) {
            component = powerDTO.getPowerComponent();
        }
        return component;
    }

    /**
     * 查找当前权限的父级权限
     *
     * @param powerSet 权限集合
     * @param dto      当前权限
     */
    private void selectPowerParent(Set<InfoPowerDTO> powerSet, InfoPowerDTO dto) {
        String parentId = "0";
        if (!parentId.equals(dto.getParentId())) {
            // 第一级权限的父级ID为0，若当前权限不是第一级，则还有父级权限
            QueryWrapper<InfoPower> wrapper = new QueryWrapper<>();
            wrapper.eq("power_id", dto.getParentId());
            InfoPower parentPower = powerMapper.selectOne(wrapper);

            InfoPowerDTO parentPowerDTO = convertEntity(parentPower);
            powerSet.add(parentPowerDTO);
            selectPowerParent(powerSet, parentPowerDTO);
        }
    }

    /**
     * 递归查询子权限
     *
     * @param parent   第一级权限
     * @param powerSet 权限集合
     * @return
     */
    public InfoPowerDTO selectPowerChild(InfoPowerDTO parent, Set<InfoPowerDTO> powerSet) {
        powerSet.forEach(infoPowerDTO -> {
            if (Objects.equals(parent.getPowerId(), infoPowerDTO.getParentId())) {
                if (Help.isEmpty(parent.getChildren())) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(infoPowerDTO);
                infoPowerDTO = selectPowerChild(infoPowerDTO, powerSet);
            }
        });
        if (Help.isNotEmpty(parent.getChildren())) {
            parent.getChildren().sort(Comparator.comparing(InfoPowerDTO::getPowerOrder));
        }
        return parent;
    }

    /**
     * 将 数据库对象 转为 数据传输对象
     *
     * @param entity 数据库信息
     * @return dto实体类
     */
    private InfoPowerDTO convertEntity(InfoPower entity) {
        InfoPowerDTO target = new InfoPowerDTO();
        if (Help.isNotEmpty(entity)) {
            BeanUtils.copyProperties(entity, target);
        }
        return target;
    }

    /**
     * 将 页面信息 转为 数据库对象
     *
     * @param info 权限 页面信息
     * @return 数据库对应实体类
     */
    private InfoPower convertInfo(InfoPowerInfo info) {
        InfoPower target = new InfoPower();
        if (Help.isNotEmpty(info)) {
            BeanUtils.copyProperties(info, target);
        }
        return target;
    }
}
