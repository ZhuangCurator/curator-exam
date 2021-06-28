package com.curator.backend.info.power.service;

import com.curator.backend.info.power.entity.dto.InfoPowerDTO;
import com.curator.backend.info.power.entity.dto.RouterDTO;
import com.curator.backend.info.power.entity.vo.info.InfoPowerInfo;
import com.curator.backend.info.power.entity.vo.search.InfoPowerSearch;
import com.curator.common.support.ResultResponse;

import java.util.List;

/**
 * <p>
 * 权限信息 服务类
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
public interface InfoPowerService {

    /**
     * 获取用户路由列表
     *
     * @return 树状路由
     */
    ResultResponse<List<RouterDTO>> selectRouter();

    /**
     * 树状权限查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<List<InfoPowerDTO>> treeWithInfoPower(InfoPowerSearch search);

    /**
     * 个人权限查询
     *
     * @return {@link ResultResponse}
     */
    ResultResponse<List<InfoPowerDTO>> personalTreeWithInfoPower();

    /**
     * 查询权限
     *
     * @param infoPowerId 权限ID
     * @return {@link ResultResponse}
     */
    ResultResponse<InfoPowerDTO> getInfoPower(String infoPowerId);

    /**
     * 添加权限
     *
     * @param info 权限信息
     * @return {@link ResultResponse}
     */
    ResultResponse<InfoPowerDTO> saveInfoPower(InfoPowerInfo info);

    /**
     * 编辑权限
     *
     * @param info 权限信息
     * @return {@link ResultResponse}
     */
    ResultResponse<InfoPowerDTO> putInfoPower(InfoPowerInfo info);

    /**
     * 删除权限
     *
     * @param infoPowerId 权限ID
     * @return {@link ResultResponse}
     */
    ResultResponse<String> removeInfoPower(String infoPowerId);

    /**
     * 编辑权限状态
     *
     * @param info 权限信息
     * @return
     */
    ResultResponse<String> changePowerStatus(InfoPowerInfo info);
}
