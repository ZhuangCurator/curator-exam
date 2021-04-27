package com.curator.backend.info.menu.controller;

import com.curator.backend.info.menu.entity.dto.InfoMenuDTO;
import com.curator.backend.info.menu.entity.vo.info.InfoMenuInfo;
import com.curator.backend.info.menu.entity.vo.search.InfoMenuSearch;
import com.curator.backend.info.menu.service.InfoMenuService;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 菜单信息 前端控制器
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
@RestController
@RequestMapping("/infoMenu")
public class InfoMenuController {

    @Autowired
    private InfoMenuService menuService;

    /**
     * 菜单分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/page")
    @Log(controllerName = "InfoMenuController", remark = "菜单分页查询")
    ResultResponse<PageResult<InfoMenuDTO>> pageWithInfoMenu(InfoMenuSearch search, HttpServletRequest request) {
        return menuService.pageWithInfoMenu(search, request);
    }

    /**
     * 菜单列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/list")
    @Log(controllerName = "InfoMenuController", remark = "菜单列表查询")
    ResultResponse<List<InfoMenuDTO>> listWithInfoMenu(InfoMenuSearch search, HttpServletRequest request) {
        return menuService.listWithInfoMenu(search, request);
    }

    /**
     * 查询菜单
     *
     * @param infoMenuId 菜单ID
     * @return {@link ResultResponse}
     */
    @GetMapping("/{infoMenuId}")
    @Log(controllerName = "InfoMenuController", remark = "菜单单个查询")
    ResultResponse<InfoMenuDTO> getInfoMenu(@PathVariable("infoMenuId") String infoMenuId) {
        return menuService.getInfoMenu(infoMenuId);
    }

    /**
     * 添加菜单
     *
     * @param info 菜单信息
     * @return {@link ResultResponse}
     */
    @PostMapping
    @Log(controllerName = "InfoMenuController", remark = "菜单添加")
    ResultResponse<InfoMenuDTO> saveInfoMenu(@RequestBody InfoMenuInfo info) {
        return menuService.saveInfoMenu(info);
    }

    /**
     * 编辑菜单
     *
     * @param info 菜单信息
     * @return {@link ResultResponse}
     */
    @PutMapping
    @Log(controllerName = "InfoMenuController", remark = "菜单编辑")
    ResultResponse<InfoMenuDTO> putInfoMenu(@RequestBody InfoMenuInfo info) {
        return menuService.putInfoMenu(info);
    }

    /**
     * 删除菜单
     *
     * @param infoMenuId 菜单ID
     * @return {@link ResultResponse}
     */
    @DeleteMapping("/{infoMenuId}")
    @Log(controllerName = "InfoMenuController", remark = "菜单删除")
    ResultResponse<String> removeInfoMenu(@PathVariable("infoMenuId") String infoMenuId) {
        return menuService.removeInfoMenu(infoMenuId);
    }
}
