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

    /**
     * 角色类型 1:超级管理员,2:超级管理员子账号,3:省级管理员,4:省级管理员子账号,5:市级管理员,6:市级管理员子账号
     */
    private Integer roleType;

}
