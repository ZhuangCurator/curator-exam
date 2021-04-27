package com.curator.backend.info.menu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.api.info.enums.InfoMenuStatusEnum;
import com.curator.backend.info.menu.entity.InfoMenu;
import com.curator.backend.info.menu.entity.dto.InfoMenuDTO;
import com.curator.backend.info.menu.entity.vo.info.InfoMenuInfo;
import com.curator.backend.info.menu.entity.vo.search.InfoMenuSearch;
import com.curator.backend.info.menu.mapper.InfoMenuMapper;
import com.curator.backend.info.menu.service.InfoMenuService;
import com.curator.common.constant.CommonConstant;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单信息 服务实现类
 *
 * @author Jun
 * @date 2021/4/17
 */
@Service
public class InfoMenuServiceImpl implements InfoMenuService {

    @Autowired
    private InfoMenuMapper menuMapper;

    @Override
    public ResultResponse<PageResult<InfoMenuDTO>> pageWithInfoMenu(InfoMenuSearch search, HttpServletRequest request) {
        Page<InfoMenu> page = new Page<>(search.getCurrent(), search.getPageSize());
        QueryWrapper<InfoMenu> wrapper = new QueryWrapper<>();
        wrapper.like(Help.isNotEmpty(search.getMenuName()), "menu_name", search.getMenuName())
                .eq(Help.isNotEmpty(search.getMenuType()), "menu_type", search.getMenuType())
                .eq(Help.isNotEmpty(search.getMenuStatus()), "menu_status", search.getMenuStatus())
                .orderByDesc("create_time");
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = request.getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(w -> w.eq("parent_account_id", createAccountId)));
        }
        IPage<InfoMenu> iPage = menuMapper.selectPage(page, wrapper);
        List<InfoMenuDTO> resultList = iPage.getRecords().stream()
                .map(this::convertEntity).collect(Collectors.toList());

        PageResult<InfoMenuDTO> resultPage = new PageResult<>();
        BeanUtils.copyProperties(iPage, resultPage, "records");
        resultPage.setRecords(resultList);

        return ResultResponse.<PageResult<InfoMenuDTO>>builder().success("菜单分页查询成功").data(resultPage).build();
    }

    @Override
    public ResultResponse<List<InfoMenuDTO>> listWithInfoMenu(InfoMenuSearch search, HttpServletRequest request) {
        QueryWrapper<InfoMenu> wrapper = new QueryWrapper<>();
        wrapper.like(Help.isNotEmpty(search.getMenuName()), "menu_name", search.getMenuName())
                .eq("menu_status", InfoMenuStatusEnum.ENABLE.getStatus())
                .orderByDesc("create_time");
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = request.getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(w -> w.eq("parent_account_id", createAccountId)));
        }
        List<InfoMenu> list = menuMapper.selectList(wrapper);
        List<InfoMenuDTO> resultList = list.stream().map(this::convertEntity).collect(Collectors.toList());

        return ResultResponse.<List<InfoMenuDTO>>builder().success("菜单列表查询成功").data(resultList).build();
    }

    @Override
    public ResultResponse<InfoMenuDTO> getInfoMenu(String infoMenuId) {
        InfoMenu entity = menuMapper.selectById(infoMenuId);
        return ResultResponse.<InfoMenuDTO>builder().success("菜单查询成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<InfoMenuDTO> saveInfoMenu(InfoMenuInfo info) {
        InfoMenu entity = convertInfo(info);
        menuMapper.insert(entity);
        return ResultResponse.<InfoMenuDTO>builder().success("菜单添加成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<InfoMenuDTO> putInfoMenu(InfoMenuInfo info) {
        InfoMenu entity = convertInfo(info);
        menuMapper.update(entity, new UpdateWrapper<InfoMenu>().eq("menu_id", info.getMenuId()));
        return ResultResponse.<InfoMenuDTO>builder().success("菜单更新成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<String> removeInfoMenu(String infoMenuId) {
        menuMapper.deleteById(infoMenuId);
        return ResultResponse.<String>builder().success("菜单删除成功").data(infoMenuId).build();
    }

    /**
     * 将 数据库对象 转为 数据传输对象
     *
     * @param entity 数据库信息
     * @return dto实体类
     */
    private InfoMenuDTO convertEntity(InfoMenu entity) {
        InfoMenuDTO target = new InfoMenuDTO();
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
    private InfoMenu convertInfo(InfoMenuInfo info) {
        InfoMenu target = new InfoMenu();
        if (Help.isNotEmpty(info)) {
            BeanUtils.copyProperties(info, target);
        }
        return target;
    }
}
