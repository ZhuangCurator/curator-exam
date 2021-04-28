package com.curator.backend.info.power.controller;

import com.curator.backend.info.power.entity.dto.InfoPowerDTO;
import com.curator.backend.info.power.entity.vo.info.InfoPowerInfo;
import com.curator.backend.info.power.entity.vo.search.InfoPowerSearch;
import com.curator.backend.info.power.service.InfoPowerService;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 权限信息 前端控制器
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
@RestController
@RequestMapping("/infoPower")
public class InfoPowerController {

    @Autowired
    private InfoPowerService powerService;

    /**
     * 权限分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/page")
    @Log(controllerName = "InfoPowerController", remark = "权限分页查询")
    ResultResponse<PageResult<InfoPowerDTO>> pageWithInfoPower(InfoPowerSearch search) {
        return powerService.pageWithInfoPower(search);
    }

    /**
     * 权限列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/list")
    @Log(controllerName = "InfoPowerController", remark = "权限列表查询")
    ResultResponse<List<InfoPowerDTO>> listWithInfoPower(InfoPowerSearch search) {
        return powerService.listWithInfoPower(search);
    }

    /**
     * 查询权限
     *
     * @param infoPowerId 权限ID
     * @return {@link ResultResponse}
     */
    @GetMapping("/{infoPowerId}")
    @Log(controllerName = "InfoPowerController", remark = "权限单个查询")
    ResultResponse<InfoPowerDTO> getInfoPower(@PathVariable("infoPowerId") String infoPowerId) {
        return powerService.getInfoPower(infoPowerId);
    }

    /**
     * 添加权限
     *
     * @param info 权限信息
     * @return {@link ResultResponse}
     */
    @PostMapping
    @Log(controllerName = "InfoPowerController", remark = "权限添加")
    ResultResponse<InfoPowerDTO> saveInfoPower(@RequestBody InfoPowerInfo info) {
        return powerService.saveInfoPower(info);
    }

    /**
     * 编辑权限
     *
     * @param info 权限信息
     * @return {@link ResultResponse}
     */
    @PutMapping
    @Log(controllerName = "InfoPowerController", remark = "权限编辑")
    ResultResponse<InfoPowerDTO> putInfoPower(@RequestBody InfoPowerInfo info) {
        return powerService.putInfoPower(info);
    }

    /**
     * 删除权限
     *
     * @param infoPowerId 权限ID
     * @return {@link ResultResponse}
     */
    @DeleteMapping("/{infoPowerId}")
    @Log(controllerName = "InfoPowerController", remark = "权限删除")
    ResultResponse<String> removeInfoPower(@PathVariable("infoPowerId") String infoPowerId) {
        return powerService.removeInfoPower(infoPowerId);
    }
}
