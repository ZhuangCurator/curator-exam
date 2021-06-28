package com.curator.backend.info.power.entity.vo.search;

import com.curator.common.support.BaseSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 权限组分页查询条件
 *
 * @author Jun
 * @date 2021/4/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InfoPowerGroupSearch extends BaseSearch {

    /**
     * 权限权限组名称
     */
    private String powerGroupName;

}
