package com.curator.backend.info.role.entity.vo.search;

import com.curator.common.support.BaseSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色分页查询条件
 *
 * @author Jun
 * @date 2021/4/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InfoRoleSearch extends BaseSearch {

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色状态(1启用 2停用)
     */
    private Integer roleStatus;

}
