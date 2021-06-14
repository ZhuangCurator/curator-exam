package com.curator.core.register.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.api.register.pojo.dto.ExamSiteDTO;
import com.curator.api.register.pojo.vo.search.ExamSiteSearch;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.core.register.service.ExamSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 考点 前端控制器
 *
 * @author Jun
 * @date 2021/5/17
 */
@RestController
@RequestMapping("/examSite")
public class ExamSiteController {

    @Autowired
    private ExamSiteService examSiteService;

    /**
     * 考点分页查询
     *
     * @param search 查询条件
     * @return
     */
    @GetMapping("/page")
    @Log(controllerName = "ExamSiteController", remark = "考点分页查询")
    ResultResponse<PageResult<ExamSiteDTO>> pageWithExamSite(ExamSiteSearch search) {
        return examSiteService.pageWithExamSite(search);
    }
}
