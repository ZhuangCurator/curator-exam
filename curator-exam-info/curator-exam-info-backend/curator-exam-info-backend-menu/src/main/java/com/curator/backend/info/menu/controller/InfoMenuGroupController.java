package com.curator.backend.info.menu.controller;


import com.curator.backend.info.menu.entity.dto.InfoMenuGroupDTO;
import com.curator.backend.info.menu.entity.vo.info.InfoMenuGroupInfo;
import com.curator.backend.info.menu.entity.vo.search.InfoMenuGroupSearch;
import com.curator.backend.info.menu.service.InfoMenuGroupService;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜单组组信息  前端控制器
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
@RestController
@RequestMapping("/InfoMenuGroup")
public class InfoMenuGroupController {

    @Autowired
    private InfoMenuGroupService menuGroupService;

    /**
     * 菜单组分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/page")
    @Log(controllerName = "InfoMenuGroupController", remark = "菜单组分页查询")
    ResultResponse<PageResult<InfoMenuGroupDTO>> pageWithInfoMenuGroup(InfoMenuGroupSearch search) {
        return menuGroupService.pageWithInfoMenuGroup(search);
    }

    /**
     * 菜单组列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/list")
    @Log(controllerName = "InfoMenuGroupController", remark = "菜单组列表查询")
    ResultResponse<List<InfoMenuGroupDTO>> listWithInfoMenuGroup(InfoMenuGroupSearch search) {
        return menuGroupService.listWithInfoMenuGroup(search);
    }

    /**
     * 查询菜单组
     *
     * @param id 菜单组ID
     * @return {@link ResultResponse}
     */
    @GetMapping("/{id}")
    @Log(controllerName = "InfoMenuGroupController", remark = "查询菜单组")
    ResultResponse<InfoMenuGroupDTO> getInfoMenuGroup(@PathVariable("id") String id) {
        return menuGroupService.getInfoMenuGroup(id);
    }

    /**
     * 添加菜单组
     *
     * @param info 菜单组信息
     * @return {@link ResultResponse}
     */
    @PostMapping
    @Log(controllerName = "InfoMenuGroupController", remark = "添加菜单组")
    ResultResponse<InfoMenuGroupDTO> saveInfoMenuGroup(@RequestBody InfoMenuGroupInfo info) {
        return menuGroupService.saveInfoMenuGroup(info);
    }

    /**
     * 编辑菜单组
     *
     * @param info 菜单组信息
     * @return {@link ResultResponse}
     */
    @PutMapping
    @Log(controllerName = "InfoMenuGroupController", remark = "编辑菜单组")
    ResultResponse<InfoMenuGroupDTO> putInfoMenuGroup(@RequestBody InfoMenuGroupInfo info) {
        return menuGroupService.putInfoMenuGroup(info);
    }

    /**
     * 删除菜单组
     *
     * @param id 菜单组ID
     * @return {@link ResultResponse}
     */
    @DeleteMapping("/{id}")
    @Log(controllerName = "InfoMenuGroupController", remark = "删除菜单组")
    ResultResponse<String> removeInfoMenuGroup(@PathVariable("id") String id) {
        return menuGroupService.removeInfoMenuGroup(id);
    }
}
