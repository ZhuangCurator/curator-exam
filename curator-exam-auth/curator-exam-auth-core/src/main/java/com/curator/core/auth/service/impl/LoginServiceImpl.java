package com.curator.core.auth.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.curator.api.auth.enums.InfoAccountStatusEnum;
import com.curator.api.auth.pojo.dto.LoginAccountDTO;
import com.curator.api.auth.pojo.vo.LoginAccountInfo;
import com.curator.common.constant.CommonConstant;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.common.util.RedissonUtil;
import com.curator.common.util.SecurityUtil;
import com.curator.common.util.ServletUtil;
import com.curator.core.auth.entity.InfoAccount;
import com.curator.core.auth.mapper.InfoAccountMapper;
import com.curator.core.auth.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    public ResultResponse<Map<String, Object>> login(LoginAccountInfo info) {
        if(Help.isEmpty(info.getAccountName())) {
            return ResultResponse.<Map<String, Object>>builder().failure("账户名不能为空").build();
        }
        if(Help.isEmpty(info.getAccountPassword())) {
            return ResultResponse.<Map<String, Object>>builder().failure("密码不能为空").build();
        }
        QueryWrapper<InfoAccount> wrapper = new QueryWrapper<>();
        wrapper.eq("account_name", info.getAccountName());
        InfoAccount infoAccount = accountMapper.selectOne(wrapper);
        if(Help.isEmpty(infoAccount)) {
            return ResultResponse.<Map<String, Object>>builder().failure("该账户不存在").build();
        }
        if(!infoAccount.getAccountPassword().equals(SecurityUtil.encryptPassword(info.getAccountPassword(),infoAccount.getSalt()))){
            return ResultResponse.<Map<String, Object>>builder().failure("密码错误").build();
        }
        if(infoAccount.getAccountStatus() == InfoAccountStatusEnum.FREEZE.getStatus()) {
            return ResultResponse.<Map<String, Object>>builder().failure("对不起,您的账号已被冻结").build();
        }
        if(infoAccount.getAccountStatus() == InfoAccountStatusEnum.LOGOUT.getStatus()) {
            return ResultResponse.<Map<String, Object>>builder().failure("对不起,您的账号已被注销").build();
        }

        Map<String, Object> tokenMap = createToken(infoAccount);
        return ResultResponse.<Map<String, Object>>builder().success("登录成功!").data(tokenMap).build();
    }

    @Override
    public ResultResponse<?> logout(HttpServletRequest request) {
        String token = ServletUtil.getParameter(CommonConstant.TOKEN_HEADER);
        if(Help.isNotEmpty(token) && token.startsWith(CommonConstant.TOKEN_PREFIX)) {
            token = token.replaceAll(CommonConstant.TOKEN_PREFIX, "");
        }
        if(Help.isNotEmpty(token)) {
            RedissonUtil.deleteObject(CommonConstant.CACHE_ACCOUNT_PREFIX + token);
        }
        return ResultResponse.builder().success("注销成功!").build();
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
        long loginTime = System.currentTimeMillis();
        LoginAccountDTO accountDTO = LoginAccountDTO.builder()
                .token(token)
                .accountId(account.getAccountId())
                .parentAccountId(account.getParentAccountId())
                .accountName(account.getAccountName())
                .loginTime(loginTime)
                .expireTime(loginTime + CommonConstant.TOKEN_EXPIRE_TIME * 1000)
                .build();
        // 保存或更新用户token
        Map<String, Object> map = new HashMap<>(8);
        map.put("access_token", token);
        map.put("expire_time", CommonConstant.TOKEN_EXPIRE_TIME);
        RedissonUtil.setCacheObject(CommonConstant.CACHE_ACCOUNT_PREFIX + token, accountDTO, CommonConstant.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        return map;
    }
}
