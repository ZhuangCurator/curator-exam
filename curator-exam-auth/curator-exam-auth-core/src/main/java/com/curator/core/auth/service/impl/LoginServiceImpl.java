package com.curator.core.auth.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.curator.api.auth.enums.InfoAccountStatusEnum;
import com.curator.api.auth.pojo.dto.LoginAccountDTO;
import com.curator.api.auth.pojo.vo.LoginAccountInfo;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.common.util.SecurityUtil;
import com.curator.core.auth.entity.InfoAccount;
import com.curator.core.auth.mapper.InfoAccountMapper;
import com.curator.core.auth.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 登录服务接口 实现类
 *
 * @author Jun
 * @date 2021/4/19
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private InfoAccountMapper accountMapper;

    @Override
    public ResultResponse<String> login(LoginAccountInfo info) {
        if(Help.isEmpty(info.getAccountName())) {
            return ResultResponse.<String>builder().failure("账户名不能为空").build();
        }
        if(Help.isEmpty(info.getAccountPassword())) {
            return ResultResponse.<String>builder().failure("密码不能为空").build();
        }
        QueryWrapper<InfoAccount> wrapper = new QueryWrapper<>();
        wrapper.eq("account_name", info.getAccountName());
        InfoAccount infoAccount = accountMapper.selectOne(wrapper);
        if(Help.isEmpty(infoAccount)) {
            return ResultResponse.<String>builder().failure("该账户不存在").build();
        }
        if(!infoAccount.getAccountPassword().equals(SecurityUtil.encryptPassword(info.getAccountPassword(),infoAccount.getSalt()))){
            return ResultResponse.<String>builder().failure("密码错误").build();
        }
        if(infoAccount.getAccountStatus() == InfoAccountStatusEnum.FREEZE.getStatus()) {
            return ResultResponse.<String>builder().failure("对不起,您的账号已被冻结").build();
        }
        if(infoAccount.getAccountStatus() == InfoAccountStatusEnum.LOGOUT.getStatus()) {
            return ResultResponse.<String>builder().failure("对不起,您的账号已被注销").build();
        }
        return null;
    }

    /**
     * 创建令牌
     *
     * @param account 账户信息
     * @return
     */
    public Map<String, Object> createToken(InfoAccount account) {
        // 生成token
        String token = IdUtil.fastUUID();
        LoginAccountDTO accountDTO = LoginAccountDTO.builder()
                .token(token)
                .accountId(account.getAccountId())
                .parentAccountId(account.getParentAccountId())
                .accountName(account.getAccountName())
                .build();
        return null;
    }
}
