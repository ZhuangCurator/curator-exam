package com.curator.core.register.controller;

import com.curator.api.register.pojo.dto.ExamSubjectDTO;
import com.curator.api.register.pojo.vo.search.ExamSubjectSearch;
import com.curator.common.annotation.Log;
import com.curator.common.support.ResultResponse;
import com.curator.core.register.service.ExamSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 考试科目 前端控制器
 *
 * @author Jun
 * @date 2021/5/17
 */
@RestController
@RequestMapping("/examSubject")
public class ExamSubjectController {

    @Autowired
    private ExamSubjectService examSubjectService;

    /**
     * 考试科目列表查询
     *
     * @param search 查询条件
     * @return
     */
    @GetMapping("/list")
    @Log(controllerName = "ExamSubjectController", remark = "考试科目列表查询")
    ResultResponse<List<ExamSubjectDTO>> listWithExamSubject(ExamSubjectSearch search) {
        return examSubjectService.listWithExamSubject(search);
    }
}
