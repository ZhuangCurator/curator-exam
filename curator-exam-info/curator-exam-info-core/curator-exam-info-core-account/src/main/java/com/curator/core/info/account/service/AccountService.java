package com.curator.core.info.account.service;

import com.curator.api.info.pojo.dto.AccountDTO;
import com.curator.api.info.pojo.vo.AccountInfo;
import com.curator.common.support.ResultResponse;

import java.util.List;
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

    /**
     * 得到账户的所有层级的下级账户ID
     *
     * @param accountId 账户id
     * @return
     */
    ResultResponse<List<String>> getAllNextLevelAccount(String accountId);

    /**
     * 普通账户注册
     *
     * @param info 账户信息
     * @return {@link ResultResponse}
     */
    ResultResponse<AccountDTO> saveInfoAccount(AccountInfo info);

    /**
     * 修改账户密码或邮箱
     *
     * @param info 账户信息
     * @return
     */
    ResultResponse<?> updateAccount(AccountInfo info);

    /**
     * 查询账户
     *
     * @param accountId 账户id
     * @return
     */
    ResultResponse<AccountDTO> getAccount(String accountId);
}
