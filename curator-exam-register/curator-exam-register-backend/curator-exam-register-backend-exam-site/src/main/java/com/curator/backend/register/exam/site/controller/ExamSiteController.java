package com.curator.backend.register.exam.site.controller;

import com.curator.backend.register.exam.site.entity.dto.ExamSiteDTO;
import com.curator.backend.register.exam.site.entity.vo.info.ExamSiteInfo;
import com.curator.backend.register.exam.site.entity.vo.search.ExamSiteSearch;
import com.curator.backend.register.exam.site.service.ExamSiteService;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 考点 前端控制器
 *
 * @author Jun
 * @since 2021-05-13
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
     * @return {@link ResultResponse}
     */
    @GetMapping("/page")
    @Log(controllerName = "ExamSiteController", remark = "考点分页查询")
    ResultResponse<PageResult<ExamSiteDTO>> pageWithExamSite(ExamSiteSearch search) {
        return examSiteService.pageWithExamSite(search);
    }

    /**
     * 考点列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/list")
    @Log(controllerName = "ExamSiteController", remark = "考点列表查询")
    ResultResponse<List<ExamSiteDTO>> listWithExamSite(ExamSiteSearch search) {
        return examSiteService.listWithExamSite(search);
    }

    /**
     * 查询考点
     *
     * @param id 考点ID
     * @return {@link ResultResponse}
     */
    @GetMapping("/{id}")
    @Log(controllerName = "ExamSiteController", remark = "查询考点")
    ResultResponse<ExamSiteDTO> getExamSite(@PathVariable("id") String id) {
        return examSiteService.getExamSite(id);
    }

    /**
     * 添加考点
     *
     * @param info 考点信息
     * @return {@link ResultResponse}
     */
    @PostMapping
    @Log(controllerName = "ExamSiteController", remark = "添加考点")
    ResultResponse<ExamSiteDTO> saveExamSite(@RequestBody ExamSiteInfo info) {
        return examSiteService.saveExamSite(info);
    }

    /**
     * 编辑考点
     *
     * @param info 考点信息
     * @return {@link ResultResponse}
     */
    @PutMapping
    @Log(controllerName = "ExamSiteController", remark = "编辑考点")
    ResultResponse<ExamSiteDTO> putExamSite(@RequestBody ExamSiteInfo info) {
        return examSiteService.putExamSite(info);
    }

    /**
     * 删除考点
     *
     * @param id 考点ID
     * @return {@link ResultResponse}
     */
    @DeleteMapping("/{id}")
    @Log(controllerName = "ExamSiteController", remark = "删除考点")
    ResultResponse<String> removeExamSite(@PathVariable("id") String id) {
        return examSiteService.removeExamSite(id);
    }
}
