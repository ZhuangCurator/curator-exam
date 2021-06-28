package com.curator.api.register.pojo.vo.search;

import com.curator.common.support.BaseSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 考生报名信息 查询条件
 *
 * @author Jun
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamRegisterInfoSearch extends BaseSearch {

    private static final long serialVersionUID = 1L;

    /**
     * 账户ID
     */
    private String accountId;

}
