package com.curator.core.register.controller;

import com.curator.api.register.pojo.dto.ExamCategoryDTO;
import com.curator.api.register.pojo.vo.search.ExamCategorySearch;
import com.curator.common.annotation.Log;
import com.curator.common.support.ResultResponse;
import com.curator.core.register.service.ExamCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 考试类别 前端控制器
 *
 * @author Jun
 * @date 2021/5/17
 */
@RestController
@RequestMapping("/examCategory")
public class ExamCategoryController {

    @Autowired
    private ExamCategoryService examCategoryService;

    /**
     * 考试类别列表查询
     *
     * @param search 查询条件
     * @return
     */
    @GetMapping("/list")
    @Log(controllerName = "ExamCategoryController", remark = "考试类别列表查询")
    ResultResponse<List<ExamCategoryDTO>> listWithExamCategory(ExamCategorySearch search) {
        return examCategoryService.listWithExamCategory(search);
    }
}
