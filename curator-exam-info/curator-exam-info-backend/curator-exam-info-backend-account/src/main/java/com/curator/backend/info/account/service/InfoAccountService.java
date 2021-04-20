package com.curator.backend.info.account.service;

import com.curator.backend.info.account.entity.dto.InfoAccountDTO;
import com.curator.backend.info.account.entity.vo.info.InfoAccountInfo;
import com.curator.backend.info.account.entity.vo.search.InfoAccountSearch;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 * 账户信息 服务类
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
public interface InfoAccountService {

    /**
     * 账户分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<PageResult<InfoAccountDTO>> pageWithInfoAccount(InfoAccountSearch search);

    /**
     * 账户列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<List<InfoAccountDTO>> listWithInfoAccount(InfoAccountSearch search);

    /**
     * 查询账户
     *
     * @param infoAccountId 账户ID
     * @return {@link ResultResponse}
     */
    ResultResponse<InfoAccountDTO> getInfoAccount(String infoAccountId);

    /**
     * 添加账户
     *
     * @param info 账户信息
     * @return {@link ResultResponse}
     */
    ResultResponse<InfoAccountDTO> saveInfoAccount(InfoAccountInfo info);

    /**
     * 编辑账户
     *
     * @param info 账户信息
     * @return {@link ResultResponse}
     */
    ResultResponse<InfoAccountDTO> putInfoAccount(InfoAccountInfo info);

    /**
     * 删除账户
     *
     * @param infoAccountId 账户ID
     * @return {@link ResultResponse}
     */
    ResultResponse<String> removeInfoAccount(String infoAccountId);

    /**
     * 添加超级管理员账户(仅供测试使用)
     *
     * @param info 账户信息
     * @return {@link ResultResponse}
     */
    ResultResponse<InfoAccountDTO> saveInfoSuperAdminAccount();
}
