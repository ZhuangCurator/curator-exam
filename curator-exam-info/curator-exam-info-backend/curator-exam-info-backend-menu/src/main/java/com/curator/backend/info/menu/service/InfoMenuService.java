package com.curator.backend.info.menu.service;

import com.curator.backend.info.menu.entity.dto.InfoMenuDTO;
import com.curator.backend.info.menu.entity.vo.info.InfoMenuInfo;
import com.curator.backend.info.menu.entity.vo.search.InfoMenuSearch;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  菜单信息 服务类
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
public interface InfoMenuService {

    /**
     * 菜单分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<PageResult<InfoMenuDTO>> pageWithInfoMenu(InfoMenuSearch search);

    /**
     * 菜单列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<List<InfoMenuDTO>> listWithInfoMenu(InfoMenuSearch search);

    /**
     * 查询菜单
     *
     * @param infoMenuId 菜单ID
     * @return {@link ResultResponse}
     */
    ResultResponse<InfoMenuDTO> getInfoMenu(String infoMenuId);

    /**
     * 添加菜单
     *
     * @param info 菜单信息
     * @return {@link ResultResponse}
     */
    ResultResponse<InfoMenuDTO> saveInfoMenu(InfoMenuInfo info);

    /**
     * 编辑菜单
     *
     * @param info 菜单信息
     * @return {@link ResultResponse}
     */
    ResultResponse<InfoMenuDTO> putInfoMenu(InfoMenuInfo info);

    /**
     * 删除菜单
     *
     * @param infoMenuId 菜单ID
     * @return {@link ResultResponse}
     */
    ResultResponse<String> removeInfoMenu(String infoMenuId);
}
