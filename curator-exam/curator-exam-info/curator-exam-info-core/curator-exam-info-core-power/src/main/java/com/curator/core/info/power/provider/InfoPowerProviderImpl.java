package com.curator.core.info.power.provider;

import com.curator.api.info.provider.InfoPowerProvider;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Set;

/**
 * @author Jun
 * @date 2021/4/19
 */
@DubboService
public class InfoPowerProviderImpl implements InfoPowerProvider {

    @Override
    public Set<String> getAccountPerms(String accountId) {
        return null;
    }
}
