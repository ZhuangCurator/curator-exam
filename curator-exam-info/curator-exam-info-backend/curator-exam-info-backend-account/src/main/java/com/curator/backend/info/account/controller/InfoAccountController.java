package com.curator.backend.info.account.controller;

import com.curator.backend.info.account.entity.dto.InfoAccountDTO;
import com.curator.backend.info.account.entity.vo.info.InfoAccountInfo;
import com.curator.backend.info.account.entity.vo.search.InfoAccountSearch;
import com.curator.backend.info.account.service.InfoAccountService;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
@Controller
@RequestMapping("/infoAccount")
public class InfoAccountController {
    
    @Autowired
    private InfoAccountService accountService;
    
    /**
     * 账户分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/page")
    ResultResponse<PageResult<InfoAccountDTO>> pageWithInfoAccount(InfoAccountSearch search) {
        return accountService.pageWithInfoAccount(search);
    }

    /**
     * 账户列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/list")
    ResultResponse<List<InfoAccountDTO>> listWithInfoAccount(InfoAccountSearch search) {
        return accountService.listWithInfoAccount(search);
    }

    /**
     * 查询账户
     *
     * @param InfoAccountId 账户ID
     * @return {@link ResultResponse}
     */
    @GetMapping("/{InfoAccountId}")
    ResultResponse<InfoAccountDTO> getInfoAccount(@PathVariable("InfoAccountId") String InfoAccountId) {
        return accountService.getInfoAccount(InfoAccountId);
    }

    /**
     * 添加账户
     *
     * @param info 账户信息
     * @return {@link ResultResponse}
     */
    @PostMapping
    ResultResponse<InfoAccountDTO> saveInfoAccount(@RequestBody InfoAccountInfo info) {
        return accountService.saveInfoAccount(info);
    }

    /**
     * 编辑账户
     *
     * @param info 账户信息
     * @return {@link ResultResponse}
     */
    @PutMapping
    ResultResponse<InfoAccountDTO> putInfoAccount(@RequestBody InfoAccountInfo info) {
        return accountService.putInfoAccount(info);
    }

    /**
     * 删除账户
     *
     * @param infoAccountId 账户ID
     * @return {@link ResultResponse}
     */
    @DeleteMapping("/{infoAccountId}")
    ResultResponse<String> removeInfoAccount(@PathVariable("infoAccountId") String infoAccountId) {
        return accountService.removeInfoAccount(infoAccountId);
    }
}
