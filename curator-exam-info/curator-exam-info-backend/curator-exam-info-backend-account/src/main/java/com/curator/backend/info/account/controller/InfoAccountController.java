package com.curator.backend.info.account.controller;

import com.curator.backend.info.account.entity.dto.InfoAccountDTO;
import com.curator.backend.info.account.entity.vo.info.InfoAccountInfo;
import com.curator.backend.info.account.entity.vo.search.InfoAccountSearch;
import com.curator.backend.info.account.service.InfoAccountService;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 账户 前端控制器
 * </p>
 *
 * @author Jun
 * @since 2021-04-16
 */
@RestController
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
    @Log(controllerName = "InfoAccountController", remark = "账户分页查询")
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
    @Log(controllerName = "InfoAccountController", remark = "账户列表查询")
    ResultResponse<List<InfoAccountDTO>> listWithInfoAccount(InfoAccountSearch search) {
        return accountService.listWithInfoAccount(search);
    }

    /**
     * 查询账户
     *
     * @param id 账户ID
     * @return {@link ResultResponse}
     */
    @GetMapping("/{id}")
    @Log(controllerName = "InfoAccountController", remark = "单个查询账户")
    ResultResponse<InfoAccountDTO> getInfoAccount(@PathVariable("id") String id) {
        return accountService.getInfoAccount(id);
    }

    /**
     * 添加下一级管理员账户
     *
     * @param info 账户信息
     * @return {@link ResultResponse}
     */
    @PostMapping
    @Log(controllerName = "InfoAccountController", remark = "添加下一级管理员账户")
    ResultResponse<InfoAccountDTO> saveInfoAccount(@RequestBody InfoAccountInfo info) {
        return accountService.saveInfoAccount(info);
    }

    /**
     * 添加子账户
     *
     * @param info 账户信息
     * @return {@link ResultResponse}
     */
    @PostMapping("/sonAccount/add")
    @Log(controllerName = "InfoAccountController", remark = "添加子账户")
    ResultResponse<InfoAccountDTO> saveSonInfoAccount(@RequestBody InfoAccountInfo info) {
        return accountService.saveSonInfoAccount(info);
    }

    /**
     * 编辑账户
     *
     * @param info 账户信息
     * @return {@link ResultResponse}
     */
    @PutMapping
    @Log(controllerName = "InfoAccountController", remark = "编辑账户")
    ResultResponse<InfoAccountDTO> putInfoAccount(@RequestBody InfoAccountInfo info) {
        return accountService.putInfoAccount(info);
    }

    /**
     * 删除账户
     *
     * @param infoAccountId 账户ID
     * @return {@link ResultResponse}
     */
    @Log(controllerName = "InfoAccountController", remark = "删除账户")
    @DeleteMapping("/{infoAccountId}")
    ResultResponse<String> removeInfoAccount(@PathVariable("infoAccountId") String infoAccountId) {
        return accountService.removeInfoAccount(infoAccountId);
    }

    /**
     * 添加超级管理员账户(仅供测试时初始化超级管理员调用)
     *
     * @return {@link ResultResponse}
     */
    @PostMapping("/super/admin")
    @Log(controllerName = "InfoAccountController", remark = "添加超级管理员账户")
    ResultResponse<InfoAccountDTO> saveInfoSuperAdminAccount() {
        return accountService.saveInfoSuperAdminAccount();
    }
}
