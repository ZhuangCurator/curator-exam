package com.curator.backend.info.menu.entity.vo.search;

import com.curator.common.support.BaseSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单组分页查询条件
 *
 * @author Jun
 * @date 2021/4/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InfoMenuGroupSearch extends BaseSearch {

    /**
     * 菜单菜单组名称
     */
    private String menuGroupName;

}
