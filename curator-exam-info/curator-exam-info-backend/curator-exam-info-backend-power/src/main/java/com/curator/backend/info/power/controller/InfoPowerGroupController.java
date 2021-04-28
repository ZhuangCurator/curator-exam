package com.curator.backend.info.power.controller;


import com.curator.backend.info.power.entity.dto.InfoPowerGroupDTO;
import com.curator.backend.info.power.entity.vo.info.InfoPowerGroupInfo;
import com.curator.backend.info.power.entity.vo.search.InfoPowerGroupSearch;
import com.curator.backend.info.power.service.InfoPowerGroupService;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 权限组组信息  前端控制器
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
@RestController
@RequestMapping("/infoPowerGroup")
public class InfoPowerGroupController {

    @Autowired
    private InfoPowerGroupService powerGroupService;

    /**
     * 权限组分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/page")
    @Log(controllerName = "InfoPowerGroupController", remark = "权限组分页查询")
    ResultResponse<PageResult<InfoPowerGroupDTO>> pageWithInfoPowerGroup(InfoPowerGroupSearch search) {
        return powerGroupService.pageWithInfoPowerGroup(search);
    }

    /**
     * 权限组列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/list")
    @Log(controllerName = "InfoPowerGroupController", remark = "权限组列表查询")
    ResultResponse<List<InfoPowerGroupDTO>> listWithInfoPowerGroup(InfoPowerGroupSearch search) {
        return powerGroupService.listWithInfoPowerGroup(search);
    }

    /**
     * 查询权限组
     *
     * @param id 权限组ID
     * @return {@link ResultResponse}
     */
    @GetMapping("/{id}")
    @Log(controllerName = "InfoPowerGroupController", remark = "查询权限组")
    ResultResponse<InfoPowerGroupDTO> getInfoPowerGroup(@PathVariable("id") String id) {
        return powerGroupService.getInfoPowerGroup(id);
    }

    /**
     * 添加权限组
     *
     * @param info 权限组信息
     * @return {@link ResultResponse}
     */
    @PostMapping
    @Log(controllerName = "InfoPowerGroupController", remark = "添加权限组")
    ResultResponse<InfoPowerGroupDTO> saveInfoPowerGroup(@RequestBody InfoPowerGroupInfo info) {
        return powerGroupService.saveInfoPowerGroup(info);
    }

    /**
     * 编辑权限组
     *
     * @param info 权限组信息
     * @return {@link ResultResponse}
     */
    @PutMapping
    @Log(controllerName = "InfoPowerGroupController", remark = "编辑权限组")
    ResultResponse<InfoPowerGroupDTO> putInfoPowerGroup(@RequestBody InfoPowerGroupInfo info) {
        return powerGroupService.putInfoPowerGroup(info);
    }

    /**
     * 删除权限组
     *
     * @param id 权限组ID
     * @return {@link ResultResponse}
     */
    @DeleteMapping("/{id}")
    @Log(controllerName = "InfoPowerGroupController", remark = "删除权限组")
    ResultResponse<String> removeInfoPowerGroup(@PathVariable("id") String id) {
        return powerGroupService.removeInfoPowerGroup(id);
    }
}
