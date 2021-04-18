package com.curator.backend.info.menu.entity.vo.search;

import com.curator.common.support.BaseSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单分页查询条件
 *
 * @author Jun
 * @date 2021/4/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InfoMenuSearch extends BaseSearch {

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单类型(1菜单 2按钮 3 目录)
     */
    private Integer menuType;

    /**
     * 菜单状态(1启用 2停用)
     */
    private Integer menuStatus;

}
