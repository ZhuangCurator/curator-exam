package com.curator.backend.info.power.entity.vo.search;

import com.curator.common.support.BaseSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 权限分页查询条件
 *
 * @author Jun
 * @date 2021/4/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InfoPowerSearch extends BaseSearch {

    /**
     * 权限名称
     */
    private String powerName;

    /**
     * 权限类型(1权限 2按钮 3 目录)
     */
    private Integer powerType;

    /**
     * 权限状态(1启用 2停用)
     */
    private Integer powerStatus;

}
