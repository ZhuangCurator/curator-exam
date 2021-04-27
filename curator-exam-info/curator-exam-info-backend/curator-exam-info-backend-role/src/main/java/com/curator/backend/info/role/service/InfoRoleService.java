package com.curator.backend.info.role.service;

import com.curator.backend.info.role.entity.dto.InfoRoleDTO;
import com.curator.backend.info.role.entity.vo.info.InfoRoleInfo;
import com.curator.backend.info.role.entity.vo.search.InfoRoleSearch;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  角色信息 服务类
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
public interface InfoRoleService {

    /**
     * 角色分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<PageResult<InfoRoleDTO>> pageWithInfoRole(InfoRoleSearch search, HttpServletRequest request);

    /**
     * 角色列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<List<InfoRoleDTO>> listWithInfoRole(InfoRoleSearch search, HttpServletRequest request);

    /**
     * 查询角色
     *
     * @param id 角色ID
     * @return {@link ResultResponse}
     */
    ResultResponse<InfoRoleDTO> getInfoRole(String id);

    /**
     * 添加角色
     *
     * @param info 角色信息
     * @return {@link ResultResponse}
     */
    ResultResponse<InfoRoleDTO> saveInfoRole(InfoRoleInfo info, HttpServletRequest request);

    /**
     * 编辑角色
     *
     * @param info 角色信息
     * @return {@link ResultResponse}
     */
    ResultResponse<InfoRoleDTO> putInfoRole(InfoRoleInfo info);

    /**
     * 删除角色
     *
     * @param id 角色ID
     * @return {@link ResultResponse}
     */
    ResultResponse<String> removeInfoRole(String id);
}
