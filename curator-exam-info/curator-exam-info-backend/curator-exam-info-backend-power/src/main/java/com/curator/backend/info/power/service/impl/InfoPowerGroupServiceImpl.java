package com.curator.backend.info.power.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.backend.info.power.entity.InfoGroupPower;
import com.curator.backend.info.power.entity.InfoPowerGroup;
import com.curator.backend.info.power.entity.InfoRolePowerGroup;
import com.curator.backend.info.power.entity.dto.InfoPowerGroupDTO;
import com.curator.backend.info.power.entity.vo.info.InfoGroupPowerInfo;
import com.curator.backend.info.power.entity.vo.info.InfoPowerGroupInfo;
import com.curator.backend.info.power.entity.vo.info.InfoRolePowerGroupInfo;
import com.curator.backend.info.power.entity.vo.search.InfoPowerGroupSearch;
import com.curator.backend.info.power.mapper.InfoGroupPowerMapper;
import com.curator.backend.info.power.mapper.InfoPowerGroupMapper;
import com.curator.backend.info.power.mapper.InfoRolePowerGroupMapper;
import com.curator.backend.info.power.service.InfoPowerGroupService;
import com.curator.common.constant.CommonConstant;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.common.util.ServletUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jun
 * @date 2021/4/17
 */
@Service
public class InfoPowerGroupServiceImpl implements InfoPowerGroupService {

    @Autowired
    private InfoPowerGroupMapper powerGroupMapper;
    @Autowired
    private InfoGroupPowerMapper groupPowerMapper;
    @Autowired
    private InfoRolePowerGroupMapper rolePowerGroupMapper;

    @Override
    public ResultResponse<PageResult<InfoPowerGroupDTO>> pageWithInfoPowerGroup(InfoPowerGroupSearch search) {
        Page<InfoPowerGroup> page = new Page<>(search.getCurrent(), search.getPageSize());
        QueryWrapper<InfoPowerGroup> wrapper = new QueryWrapper<>();
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(w -> w.eq("parent_account_id", createAccountId)));
        }
        wrapper.like(Help.isNotEmpty(search.getPowerGroupName()), "power_group_name", search.getPowerGroupName())
                .orderByDesc("create_time");
        IPage<InfoPowerGroup> iPage = powerGroupMapper.selectPage(page, wrapper);
        List<InfoPowerGroupDTO> resultList = iPage.getRecords().stream()
                .map(this::convertEntity).collect(Collectors.toList());

        PageResult<InfoPowerGroupDTO> resultPage = new PageResult<>();
        BeanUtils.copyProperties(iPage, resultPage, "records");
        resultPage.setRecords(resultList);

        return ResultResponse.<PageResult<InfoPowerGroupDTO>>builder().success("权限组分页查询成功").data(resultPage).build();

    }

    @Override
    public ResultResponse<List<InfoPowerGroupDTO>> listWithInfoPowerGroup(InfoPowerGroupSearch search) {
        QueryWrapper<InfoPowerGroup> wrapper = new QueryWrapper<>();
        wrapper.like(Help.isNotEmpty(search.getPowerGroupName()), "power_group_name", search.getPowerGroupName())
                .orderByDesc("create_time");
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(w -> w.eq("parent_account_id", createAccountId)));
        }
        List<InfoPowerGroup> list = powerGroupMapper.selectList(wrapper);
        List<InfoPowerGroupDTO> resultList = list.stream().map(this::convertEntity).collect(Collectors.toList());

        return ResultResponse.<List<InfoPowerGroupDTO>>builder().success("权限组列表查询成功").data(resultList).build();

    }

    @Override
    public ResultResponse<InfoPowerGroupDTO> getInfoPowerGroup(String infoPowerGroupId) {
        InfoPowerGroup entity = powerGroupMapper.selectById(infoPowerGroupId);
        return ResultResponse.<InfoPowerGroupDTO>builder().success("权限组查询成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<InfoPowerGroupDTO> saveInfoPowerGroup(InfoPowerGroupInfo info) {
        InfoPowerGroup entity = convertInfo(info);
        entity.setCreateAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID));
        entity.setParentAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_PARENT_ID));
        powerGroupMapper.insert(entity);
        return ResultResponse.<InfoPowerGroupDTO>builder().success("权限组添加成功").data(convertEntity(entity)).build();

    }

    @Override
    public ResultResponse<InfoPowerGroupDTO> putInfoPowerGroup(InfoPowerGroupInfo info) {
        InfoPowerGroup entity = convertInfo(info);
        powerGroupMapper.update(entity, new UpdateWrapper<InfoPowerGroup>().eq("power_group_id", info.getPowerGroupId()));
        return ResultResponse.<InfoPowerGroupDTO>builder().success("权限组更新成功").data(convertEntity(entity)).build();

    }

    @Override
    public ResultResponse<String> removeInfoPowerGroup(String infoPowerGroupId) {
        powerGroupMapper.deleteById(infoPowerGroupId);
        // 同时删除权限组与角色的关联关系
        QueryWrapper<InfoRolePowerGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("power_group_id", infoPowerGroupId);
        rolePowerGroupMapper.delete(wrapper);
        return ResultResponse.<String>builder().success("权限组删除成功").data(infoPowerGroupId).build();
    }

    @Override
    public ResultResponse<?> addPowerToPowerGroup(InfoGroupPowerInfo info) {
        if(Help.isNotEmpty(info.getPowerIdList())) {
            // 首先删除原有的绑定关系
            QueryWrapper<InfoGroupPower> wrapper = new QueryWrapper<>();
            wrapper.eq("power_group_id", info.getPowerGroupId());
            groupPowerMapper.delete(wrapper);
            info.getPowerIdList().forEach(powerId -> {
                InfoGroupPower groupPower = new InfoGroupPower();
                groupPower.setPowerGroupId(info.getPowerGroupId());
                groupPower.setPowerId(powerId);
                groupPower.setCreateAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID));
                groupPower.setParentAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_PARENT_ID));
                groupPowerMapper.insert(groupPower);
            });
            return ResultResponse.builder().success("权限列表成功添加至权限组!").build();
        }
        return ResultResponse.builder().failure("权限列表不能为空!").build();
    }

    @Override
    public ResultResponse<?> bindPowerGroupWithRole(InfoRolePowerGroupInfo info) {
        if(Help.isNotEmpty(info.getPowerGroupIdList())) {
            info.getPowerGroupIdList().forEach(powerGroupId -> {
                QueryWrapper<InfoRolePowerGroup> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("power_group_id", powerGroupId)
                        .eq("role_id", info.getRoleId());
                Integer count = rolePowerGroupMapper.selectCount(queryWrapper);
                if(count == null || count == 0) {
                    InfoRolePowerGroup entity = new InfoRolePowerGroup();
                    entity.setPowerGroupId(powerGroupId);
                    entity.setRoleId(info.getRoleId());
                    entity.setCreateAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID));
                    entity.setParentAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_PARENT_ID));
                    rolePowerGroupMapper.insert(entity);
                }
            });
            return ResultResponse.builder().success("权限组列表成功与角色绑定!").build();
        }
        return ResultResponse.builder().failure("权限组列表不能为空!").build();
    }

    /**
     * 将 数据库对象 转为 数据传输对象
     *
     * @param entity 数据库信息
     * @return dto实体类
     */
    private InfoPowerGroupDTO convertEntity(InfoPowerGroup entity) {
        InfoPowerGroupDTO target = new InfoPowerGroupDTO();
        if (Help.isNotEmpty(entity)) {
            BeanUtils.copyProperties(entity, target);
            QueryWrapper<InfoGroupPower> wrapper = new QueryWrapper<>();
            wrapper.eq("power_group_id", entity.getPowerGroupId());
            List<InfoGroupPower> list = groupPowerMapper.selectList(wrapper);
            if(Help.isNotEmpty(list)) {
                List<String> powerIdList = list.stream().map(InfoGroupPower::getPowerId).collect(Collectors.toList());
                target.setPowerIdList(powerIdList);
            }
        }
        return target;
    }

    /**
     * 将 页面信息 转为 数据库对象
     *
     * @param info 权限组 页面信息
     * @return 数据库对应实体类
     */
    private InfoPowerGroup convertInfo(InfoPowerGroupInfo info) {
        InfoPowerGroup target = new InfoPowerGroup();
        if (Help.isNotEmpty(info)) {
            BeanUtils.copyProperties(info, target);
        }
        return target;
    }
}
