package com.curator.backend.info.power.service;

import com.curator.backend.info.power.entity.dto.InfoPowerGroupDTO;
import com.curator.backend.info.power.entity.vo.info.InfoPowerGroupInfo;
import com.curator.backend.info.power.entity.vo.search.InfoPowerGroupSearch;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 权限组信息 服务类
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
public interface InfoPowerGroupService {


    /**
     * 权限组分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<PageResult<InfoPowerGroupDTO>> pageWithInfoPowerGroup(InfoPowerGroupSearch search);

    /**
     * 权限组列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<List<InfoPowerGroupDTO>> listWithInfoPowerGroup(InfoPowerGroupSearch search);

    /**
     * 查询权限组
     *
     * @param infoPowerGroupId 权限组ID
     * @return {@link ResultResponse}
     */
    ResultResponse<InfoPowerGroupDTO> getInfoPowerGroup(String infoPowerGroupId);

    /**
     * 添加权限组
     *
     * @param info 权限组信息
     * @return {@link ResultResponse}
     */
    ResultResponse<InfoPowerGroupDTO> saveInfoPowerGroup(InfoPowerGroupInfo info);

    /**
     * 编辑权限组
     *
     * @param info 权限组信息
     * @return {@link ResultResponse}
     */
    ResultResponse<InfoPowerGroupDTO> putInfoPowerGroup(InfoPowerGroupInfo info);

    /**
     * 删除权限组
     *
     * @param infoPowerGroupId 权限组ID
     * @return {@link ResultResponse}
     */
    ResultResponse<String> removeInfoPowerGroup(String infoPowerGroupId);
}
