package com.curator.backend.info.menu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.backend.info.menu.entity.InfoMenuGroup;
import com.curator.backend.info.menu.entity.dto.InfoMenuGroupDTO;
import com.curator.backend.info.menu.entity.vo.info.InfoMenuGroupInfo;
import com.curator.backend.info.menu.entity.vo.search.InfoMenuGroupSearch;
import com.curator.backend.info.menu.mapper.InfoMenuGroupMapper;
import com.curator.backend.info.menu.service.InfoMenuGroupService;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
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
public class InfoMenuGroupServiceImpl implements InfoMenuGroupService {

    @Autowired
    private InfoMenuGroupMapper menuGroupMapper;

    @Override
    public ResultResponse<PageResult<InfoMenuGroupDTO>> pageWithInfoMenuGroup(InfoMenuGroupSearch search) {
        Page<InfoMenuGroup> page = new Page<>(search.getCurrent(), search.getPageSize());
        QueryWrapper<InfoMenuGroup> wrapper = new QueryWrapper<>();
        if (Boolean.FALSE.equals(search.getSuperAdmin()) && Help.isNotEmpty(search.getCreateAccountId())) {
            wrapper.and(wr -> wr.eq("create_account_id", search.getCreateAccountId())
                    .or(w -> w.eq("parent_account_id", search.getCreateAccountId())));
        }
        wrapper.like(Help.isNotEmpty(search.getMenuGroupName()), "menu_group_name", search.getMenuGroupName())
                .orderByDesc("create_time");
        IPage<InfoMenuGroup> iPage = menuGroupMapper.selectPage(page, wrapper);
        List<InfoMenuGroupDTO> resultList = iPage.getRecords().stream()
                .map(this::convertEntity).collect(Collectors.toList());

        PageResult<InfoMenuGroupDTO> resultPage = new PageResult<>();
        BeanUtils.copyProperties(iPage, resultPage, "records");
        resultPage.setRecords(resultList);

        return ResultResponse.<PageResult<InfoMenuGroupDTO>>builder().success("菜单组分页查询成功").data(resultPage).build();

    }

    @Override
    public ResultResponse<List<InfoMenuGroupDTO>> listWithInfoMenuGroup(InfoMenuGroupSearch search) {
        QueryWrapper<InfoMenuGroup> wrapper = new QueryWrapper<>();
        wrapper.like(Help.isNotEmpty(search.getMenuGroupName()), "menu_group_name", search.getMenuGroupName())
                .orderByDesc("create_time");
        if (Boolean.FALSE.equals(search.getSuperAdmin()) && Help.isNotEmpty(search.getCreateAccountId())) {
            wrapper.and(wr -> wr.eq("create_account_id", search.getCreateAccountId())
                    .or(w -> w.eq("parent_account_id", search.getCreateAccountId())));
        }
        List<InfoMenuGroup> list = menuGroupMapper.selectList(wrapper);
        List<InfoMenuGroupDTO> resultList = list.stream().map(this::convertEntity).collect(Collectors.toList());

        return ResultResponse.<List<InfoMenuGroupDTO>>builder().success("菜单组列表查询成功").data(resultList).build();

    }

    @Override
    public ResultResponse<InfoMenuGroupDTO> getInfoMenuGroup(String infoMenuGroupId) {
        InfoMenuGroup entity = menuGroupMapper.selectById(infoMenuGroupId);
        return ResultResponse.<InfoMenuGroupDTO>builder().success("菜单组查询成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<InfoMenuGroupDTO> saveInfoMenuGroup(InfoMenuGroupInfo info) {
        InfoMenuGroup entity = convertInfo(info);
        menuGroupMapper.insert(entity);
        return ResultResponse.<InfoMenuGroupDTO>builder().success("菜单组添加成功").data(convertEntity(entity)).build();

    }

    @Override
    public ResultResponse<InfoMenuGroupDTO> putInfoMenuGroup(InfoMenuGroupInfo info) {
        InfoMenuGroup entity = convertInfo(info);
        menuGroupMapper.update(entity, new UpdateWrapper<InfoMenuGroup>().eq("menu_group_id", info.getMenuGroupId()));
        return ResultResponse.<InfoMenuGroupDTO>builder().success("菜单组更新成功").data(convertEntity(entity)).build();

    }

    @Override
    public ResultResponse<String> removeInfoMenuGroup(String infoMenuGroupId) {
        menuGroupMapper.deleteById(infoMenuGroupId);
        return ResultResponse.<String>builder().success("菜单组删除成功").data(infoMenuGroupId).build();
    }

    /**
     * 将 数据库对象 转为 数据传输对象
     *
     * @param entity 数据库信息
     * @return dto实体类
     */
    private InfoMenuGroupDTO convertEntity(InfoMenuGroup entity) {
        InfoMenuGroupDTO target = new InfoMenuGroupDTO();
        if (Help.isNotEmpty(entity)) {
            BeanUtils.copyProperties(entity, target);
        }
        return target;
    }

    /**
     * 将 页面信息 转为 数据库对象
     *
     * @param info 菜单组 页面信息
     * @return 数据库对应实体类
     */
    private InfoMenuGroup convertInfo(InfoMenuGroupInfo info) {
        InfoMenuGroup target = new InfoMenuGroup();
        if (Help.isNotEmpty(info)) {
            BeanUtils.copyProperties(info, target);
        }
        return target;
    }
}
