package com.curator.backend.info.power.service;

import com.curator.backend.info.power.entity.dto.InfoPowerDTO;
import com.curator.backend.info.power.entity.vo.info.InfoPowerInfo;
import com.curator.backend.info.power.entity.vo.search.InfoPowerSearch;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  权限信息 服务类
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
public interface InfoPowerService {

    /**
     * 权限分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<PageResult<InfoPowerDTO>> pageWithInfoPower(InfoPowerSearch search);

    /**
     * 权限列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<List<InfoPowerDTO>> listWithInfoPower(InfoPowerSearch search);

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
}
