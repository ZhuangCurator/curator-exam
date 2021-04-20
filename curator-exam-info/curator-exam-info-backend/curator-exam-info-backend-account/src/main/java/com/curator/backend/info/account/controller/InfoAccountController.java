package com.curator.backend.info.account.controller;

import com.curator.backend.info.account.entity.dto.InfoAccountDTO;
import com.curator.backend.info.account.entity.vo.info.InfoAccountInfo;
import com.curator.backend.info.account.entity.vo.search.InfoAccountSearch;
import com.curator.backend.info.account.service.InfoAccountService;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.JsonUtil;
import com.curator.common.util.RedissonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
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

    @GetMapping("/redisson/cache")
    public void testRedissonCache() {
        String str = "I AM A STUDENT";
        RedissonUtil.setCacheObject("str", str);
        String cacheStr = RedissonUtil.getCacheObject("str");
        System.out.println(cacheStr);
        System.out.println("================================");
        ResultResponse<String> resultResponse = ResultResponse.<String>builder().success("成功").data("测试").build();
        RedissonUtil.setCacheObject("res", resultResponse);
        ResultResponse<String> cacheRes = RedissonUtil.getCacheObject("res");
        System.out.println(JsonUtil.obj2String(cacheRes));
        System.out.println("================================");
        List<String> list = Arrays.asList("aa", "bb", "cc");
        RedissonUtil.setCacheList("list", list);
        RedissonUtil.setCacheListValue("list", "dd");
        List<String> cacheList = RedissonUtil.getCacheList("list");
        System.out.println(JsonUtil.obj2String(cacheList));
    }
    
    /**
     * 账户分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/page")
    @Log(controllerName = "InfoAccountController", remark = "账户分页查询")
    ResultResponse<PageResult<InfoAccountDTO>> pageWithInfoAccount(HttpServletRequest re, InfoAccountSearch search) {
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
     * @param InfoAccountId 账户ID
     * @return {@link ResultResponse}
     */
    @GetMapping("/{InfoAccountId}")
    @Log(controllerName = "InfoAccountController", remark = "单个查询账户")
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
    @Log(controllerName = "InfoAccountController", remark = "添加账户")
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
     * 添加超级管理员账户(仅供测试使用)
     *
     * @param info 账户信息
     * @return {@link ResultResponse}
     */
    @PostMapping
    @Log(controllerName = "InfoAccountController", remark = "添加超级管理员账户")
    ResultResponse<InfoAccountDTO> saveInfoSuperAdminAccount() {
        return accountService.saveInfoSuperAdminAccount();
    }
}
