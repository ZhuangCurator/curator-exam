package com.curator.core.info.account.service;

import com.curator.common.support.ResultResponse;

import java.util.Set;

/**
 * @author Jun
 * @date 2021/4/19
 */
public interface AccountService {

    /**
     * 查询账户所拥有的全部权限标识
     *
     * @param accountId 账户id
     * @return
     */
    ResultResponse<Set<String>> getAccountAllPerms(String accountId);
}
