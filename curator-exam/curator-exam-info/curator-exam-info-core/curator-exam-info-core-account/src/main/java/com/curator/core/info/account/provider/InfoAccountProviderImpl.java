package com.curator.core.info.account.provider;

import com.curator.api.info.pojo.dto.AccountDTO;
import com.curator.api.info.provider.InfoAccountProvider;
import com.curator.common.support.ResultResponse;
import com.curator.core.info.account.service.AccountService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

/**
 * @author Jun
 * @date 2021/4/19
 */
@DubboService
public class InfoAccountProviderImpl implements InfoAccountProvider {

    @Autowired
    private AccountService accountService;

    @Override
    public ResultResponse<Set<String>> getAccountAllPerms(String accountId) {
        return accountService.getAccountAllPerms(accountId);
    }

    @Override
    public ResultResponse<List<String>> getAllNextLevelAccount(String accountId) {
        return accountService.getAllNextLevelAccount(accountId);
    }

    @Override
    public ResultResponse<AccountDTO> getAccount(String accountId) {
        return accountService.getAccount(accountId);
    }
}
