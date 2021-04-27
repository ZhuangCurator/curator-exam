package com.curator.core.log.controller;

import com.curator.api.log.pojo.dto.InfoRequestLogDTO;
import com.curator.api.log.pojo.vo.seacrh.InfoRequestLogSearch;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.core.log.service.InfoRequestLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 请求日志信息 前端控制器
 * </p>
 *
 * @author Jun
 * @since 2021-04-17
 */
@RestController
@RequestMapping("/infoRequestLog")
public class InfoRequestLogController {

    @Autowired
    private InfoRequestLogService requestLogService;

    /**
     * 请求日志信息 分页查询
     *
     * @param search {@link InfoRequestLogSearch}
     * @return 分页结果
     */
    @GetMapping("/page")
    ResultResponse<PageResult<InfoRequestLogDTO>> pageWithRequestLog(InfoRequestLogSearch search, HttpServletRequest request){
        return requestLogService.pageWithRequestLog(search, request);
    }
}
