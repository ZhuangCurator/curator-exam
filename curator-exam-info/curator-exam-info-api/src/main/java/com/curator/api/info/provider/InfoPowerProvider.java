package com.curator.api.info.provider;

import java.util.Set;

/**
 * 权限信息 RPC接口
 *
 * @author Jun
 * @date 2021/4/19
 */
public interface InfoPowerProvider {

    /**
     * 获取账户的全部权限标识
     *
     * @param accountId 账户id
     * @return 权限标识集合
     */
    Set<String> getAccountPerms(String accountId);
}
