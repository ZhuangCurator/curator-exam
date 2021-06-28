package com.curator.backend.info.account.entity.vo.search;

import com.curator.common.support.BaseSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 账户分页查询条件
 *
 * @author Jun
 * @date 2021/4/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InfoAccountSearch extends BaseSearch {

    /**
     * 账户名称
     */
    private String accountName;

    /**
     * 账户状态
     */
    private Integer accountStatus;

    /**
     * 是否查询子账号
     */
    private Boolean child = Boolean.FALSE;

    /**
     * 是否查询普通账户
     */
    private Boolean ordinary = Boolean.FALSE;

}
