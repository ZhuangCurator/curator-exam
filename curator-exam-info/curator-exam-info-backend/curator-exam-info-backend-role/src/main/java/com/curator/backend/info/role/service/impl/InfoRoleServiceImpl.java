package com.curator.backend.info.role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.api.info.enums.InfoRoleStatusEnum;
import com.curator.backend.info.role.entity.InfoRole;
import com.curator.backend.info.role.entity.dto.InfoRoleDTO;
import com.curator.backend.info.role.entity.vo.info.InfoRoleInfo;
import com.curator.backend.info.role.entity.vo.search.InfoRoleSearch;
import com.curator.backend.info.role.mapper.InfoRoleMapper;
import com.curator.backend.info.role.service.InfoRoleService;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  角色信息 服务实现类
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
@Service
public class InfoRoleServiceImpl implements InfoRoleService {
    
    @Autowired
    private InfoRoleMapper roleMapper;
    
    @Override
    public ResultResponse<PageResult<InfoRoleDTO>> pageWithInfoRole(InfoRoleSearch search) {
        Page<InfoRole> page = new Page<>(search.getCurrent(), search.getPageSize());
        QueryWrapper<InfoRole> wrapper = new QueryWrapper<>();
        wrapper.like(Help.isNotEmpty(search.getRoleName()), "role_name", search.getRoleName())
                .eq(Help.isNotEmpty(search.getRoleStatus()), "role_status", search.getRoleStatus())
                .orderByDesc("create_time");
        if (Boolean.FALSE.equals(search.getSuperAdmin()) && Help.isNotEmpty(search.getCreateAccountId())) {
            wrapper.and(wr -> wr.eq("create_account_id", search.getCreateAccountId())
                    .or(w -> w.eq("parent_account_id", search.getCreateAccountId())));
        }
        IPage<InfoRole> iPage = roleMapper.selectPage(page, wrapper);
        List<InfoRoleDTO> resultList = iPage.getRecords().stream()
                .map(this::convertEntity).collect(Collectors.toList());

        PageResult<InfoRoleDTO> resultPage = new PageResult<>();
        BeanUtils.copyProperties(iPage, resultPage, "records");
        resultPage.setRecords(resultList);

        return ResultResponse.<PageResult<InfoRoleDTO>>builder().success("菜单分页查询成功").data(resultPage).build();
    }

    @Override
    public ResultResponse<List<InfoRoleDTO>> listWithInfoRole(InfoRoleSearch search) {
        QueryWrapper<InfoRole> wrapper = new QueryWrapper<>();
        wrapper.like(Help.isNotEmpty(search.getRoleName()), "role_name", search.getRoleName())
                .eq("role_status", InfoRoleStatusEnum.ENABLE.getStatus())
                .orderByDesc("create_time");
        if (Boolean.FALSE.equals(search.getSuperAdmin()) && Help.isNotEmpty(search.getCreateAccountId())) {
            wrapper.and(wr -> wr.eq("create_account_id", search.getCreateAccountId())
                    .or(w -> w.eq("parent_account_id", search.getCreateAccountId())));
        }
        List<InfoRole> list = roleMapper.selectList(wrapper);
        List<InfoRoleDTO> resultList = list.stream().map(this::convertEntity).collect(Collectors.toList());

        return ResultResponse.<List<InfoRoleDTO>>builder().success("菜单列表查询成功").data(resultList).build();
    }

    @Override
    public ResultResponse<InfoRoleDTO> getInfoRole(String id) {
        InfoRole entity = roleMapper.selectById(id);
        return ResultResponse.<InfoRoleDTO>builder().success("菜单查询成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<InfoRoleDTO> saveInfoRole(InfoRoleInfo info) {
        InfoRole entity = convertInfo(info);
        roleMapper.insert(entity);
        return ResultResponse.<InfoRoleDTO>builder().success("菜单添加成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<InfoRoleDTO> putInfoRole(InfoRoleInfo info) {
        InfoRole entity = convertInfo(info);
        roleMapper.update(entity, new UpdateWrapper<InfoRole>().eq("Role_id", info.getRoleId()));
        return ResultResponse.<InfoRoleDTO>builder().success("菜单更新成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<String> removeInfoRole(String id) {
        roleMapper.deleteById(id);
        return ResultResponse.<String>builder().success("菜单删除成功").data(id).build();
    }

    /**
     * 将 数据库对象 转为 数据传输对象
     *
     * @param entity 数据库信息
     * @return dto实体类
     */
    private InfoRoleDTO convertEntity(InfoRole entity) {
        InfoRoleDTO target = new InfoRoleDTO();
        if (Help.isNotEmpty(entity)) {
            BeanUtils.copyProperties(entity, target);
        }
        return target;
    }

    /**
     * 将 页面信息 转为 数据库对象
     *
     * @param info 菜单 页面信息
     * @return 数据库对应实体类
     */
    private InfoRole convertInfo(InfoRoleInfo info) {
        InfoRole target = new InfoRole();
        if (Help.isNotEmpty(info)) {
            BeanUtils.copyProperties(info, target);
        }
        return target;
    }
}
