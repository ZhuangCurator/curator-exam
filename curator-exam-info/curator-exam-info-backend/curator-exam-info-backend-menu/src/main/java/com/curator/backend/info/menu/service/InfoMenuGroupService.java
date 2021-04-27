package com.curator.backend.info.menu.service;

import com.curator.backend.info.menu.entity.dto.InfoMenuGroupDTO;
import com.curator.backend.info.menu.entity.vo.info.InfoMenuGroupInfo;
import com.curator.backend.info.menu.entity.vo.search.InfoMenuGroupSearch;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 菜单组信息 服务类
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
public interface InfoMenuGroupService {


    /**
     * 菜单组分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<PageResult<InfoMenuGroupDTO>> pageWithInfoMenuGroup(InfoMenuGroupSearch search, HttpServletRequest request);

    /**
     * 菜单组列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<List<InfoMenuGroupDTO>> listWithInfoMenuGroup(InfoMenuGroupSearch search, HttpServletRequest request);

    /**
     * 查询菜单组
     *
     * @param infoMenuGroupId 菜单组ID
     * @return {@link ResultResponse}
     */
    ResultResponse<InfoMenuGroupDTO> getInfoMenuGroup(String infoMenuGroupId);

    /**
     * 添加菜单组
     *
     * @param info 菜单组信息
     * @return {@link ResultResponse}
     */
    ResultResponse<InfoMenuGroupDTO> saveInfoMenuGroup(InfoMenuGroupInfo info);

    /**
     * 编辑菜单组
     *
     * @param info 菜单组信息
     * @return {@link ResultResponse}
     */
    ResultResponse<InfoMenuGroupDTO> putInfoMenuGroup(InfoMenuGroupInfo info);

    /**
     * 删除菜单组
     *
     * @param infoMenuGroupId 菜单组ID
     * @return {@link ResultResponse}
     */
    ResultResponse<String> removeInfoMenuGroup(String infoMenuGroupId);
}
