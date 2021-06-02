package com.curator.api.info.provider;

import com.curator.api.info.pojo.dto.AccountDTO;
import com.curator.common.support.ResultResponse;

import java.util.Set;

/**
 * 账户信息 RPC接口
 *
 * @author Jun
 * @date 2021/4/19
 */
public interface InfoAccountProvider {

    /**
     * 查询账户所拥有的全部权限标识
     *
     * @param accountId 账户id
     * @return
     */
    ResultResponse<Set<String>> getAccountAllPerms(String accountId);

    /**
     * 查询账户
     *
     * @param accountId 账户id
     * @return
     */
    ResultResponse<AccountDTO> getAccount(String accountId);
}
