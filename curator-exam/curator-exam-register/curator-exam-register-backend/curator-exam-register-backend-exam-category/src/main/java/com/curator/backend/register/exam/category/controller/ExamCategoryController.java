package com.curator.backend.register.exam.category.controller;

import com.curator.backend.register.exam.category.entity.dto.ExamCategoryDTO;
import com.curator.backend.register.exam.category.entity.vo.info.ExamCategoryInfo;
import com.curator.backend.register.exam.category.entity.vo.search.ExamCategorySearch;
import com.curator.backend.register.exam.category.service.ExamCategoryService;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 考试类别 前端控制器
 *
 * @author Jun
 * @since 2021-05-13
 */
@RestController
@RequestMapping("/examCategory")
public class ExamCategoryController {

    @Autowired
    private ExamCategoryService examCategoryService;

    /**
     * 考试类别分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/page")
    @Log(controllerName = "ExamCategoryController", remark = "考试类别分页查询")
    ResultResponse<PageResult<ExamCategoryDTO>> pageWithExamCategory(ExamCategorySearch search) {
        return examCategoryService.pageWithExamCategory(search);
    }

    /**
     * 考试类别列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/list")
    @Log(controllerName = "ExamCategoryController", remark = "考试类别列表查询")
    ResultResponse<List<ExamCategoryDTO>> listWithExamCategory(ExamCategorySearch search) {
        return examCategoryService.listWithExamCategory(search);
    }

    /**
     * 查询考试类别
     *
     * @param id 考试类别ID
     * @return {@link ResultResponse}
     */
    @GetMapping("/{id}")
    @Log(controllerName = "ExamCategoryController", remark = "查询考试类别")
    ResultResponse<ExamCategoryDTO> getExamCategory(@PathVariable("id") String id) {
        return examCategoryService.getExamCategory(id);
    }

    /**
     * 添加考试类别
     *
     * @param info 考试类别信息
     * @return {@link ResultResponse}
     */
    @PostMapping
    @Log(controllerName = "ExamCategoryController", remark = "添加考试类别")
    ResultResponse<ExamCategoryDTO> saveExamCategory(@RequestBody ExamCategoryInfo info) {
        return examCategoryService.saveExamCategory(info);
    }

    /**
     * 编辑考试类别
     *
     * @param info 考试类别信息
     * @return {@link ResultResponse}
     */
    @PutMapping
    @Log(controllerName = "ExamCategoryController", remark = "编辑考试类别")
    ResultResponse<ExamCategoryDTO> putExamCategory(@RequestBody ExamCategoryInfo info) {
        return examCategoryService.putExamCategory(info);
    }

    /**
     * 删除考试类别
     *
     * @param id 考试类别ID
     * @return {@link ResultResponse}
     */
    @DeleteMapping("/{id}")
    @Log(controllerName = "ExamCategoryController", remark = "删除考试类别")
    ResultResponse<String> removeExamCategory(@PathVariable("id") String id) {
        return examCategoryService.removeExamCategory(id);
    }

}
