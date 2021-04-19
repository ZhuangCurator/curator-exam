package com.curator.core.info.menu.provider;

import com.curator.api.info.provider.InfoMenuProvider;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Set;

/**
 * @author Jun
 * @date 2021/4/19
 */
@DubboService
public class InfoMenuProviderImpl implements InfoMenuProvider {

    @Override
    public Set<String> getAccountPerms(String accountId) {
        return null;
    }
}
