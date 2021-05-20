package com.curator.backend.info.role.controller;

import com.curator.backend.info.role.entity.dto.InfoRoleDTO;
import com.curator.backend.info.role.entity.vo.info.InfoRoleInfo;
import com.curator.backend.info.role.entity.vo.search.InfoRoleSearch;
import com.curator.backend.info.role.service.InfoRoleService;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色信息 前端控制器
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
@RestController
@RequestMapping("/infoRole")
public class InfoRoleController {

    @Autowired
    private InfoRoleService roleService;

    /**
     * 角色分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/page")
    @Log(controllerName = "InfoRoleController", remark = "角色分页查询")
    ResultResponse<PageResult<InfoRoleDTO>> pageWithInfoRole(InfoRoleSearch search) {
        return roleService.pageWithInfoRole(search);
    }

    /**
     * 角色列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/list")
    @Log(controllerName = "InfoRoleController", remark = "角色列表查询")
    ResultResponse<List<InfoRoleDTO>> listWithInfoRole(InfoRoleSearch search) {
        return roleService.listWithInfoRole(search);
    }

    /**
     * 查询角色
     *
     * @param id 角色ID
     * @return {@link ResultResponse}
     */
    @GetMapping("/{id}")
    @Log(controllerName = "InfoRoleController", remark = "查询角色")
    ResultResponse<InfoRoleDTO> getInfoRole(@PathVariable("id") String id) {
        return roleService.getInfoRole(id);
    }

    /**
     * 添加角色
     *
     * @param info 角色信息
     * @return {@link ResultResponse}
     */
    @PostMapping
    @Log(controllerName = "InfoRoleController", remark = "添加角色")
    ResultResponse<InfoRoleDTO> saveInfoRole(@RequestBody InfoRoleInfo info) {
        return roleService.saveInfoRole(info);
    }

    /**
     * 编辑角色
     *
     * @param info 角色信息
     * @return {@link ResultResponse}
     */
    @PutMapping
    @Log(controllerName = "InfoRoleController", remark = "编辑角色")
    ResultResponse<InfoRoleDTO> putInfoRole(@RequestBody InfoRoleInfo info) {
        return roleService.putInfoRole(info);
    }

    /**
     * 删除角色
     *
     * @param id 角色ID
     * @return {@link ResultResponse}
     */
    @DeleteMapping("/{id}")
    @Log(controllerName = "InfoRoleController", remark = "删除角色")
    ResultResponse<String> removeInfoRole(@PathVariable("id") String id) {
        return roleService.removeInfoRole(id);
    }
}
