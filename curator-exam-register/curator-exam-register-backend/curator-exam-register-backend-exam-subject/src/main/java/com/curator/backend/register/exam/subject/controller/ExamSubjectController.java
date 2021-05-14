package com.curator.backend.register.exam.subject.controller;

import com.curator.backend.register.exam.subject.entity.dto.ExamSubjectDTO;
import com.curator.backend.register.exam.subject.entity.dto.ExamSubjectSiteDTO;
import com.curator.backend.register.exam.subject.entity.vo.info.ExamSubjectInfo;
import com.curator.backend.register.exam.subject.entity.vo.info.ExamSubjectSiteInfo;
import com.curator.backend.register.exam.subject.entity.vo.search.ExamSubjectSearch;
import com.curator.backend.register.exam.subject.entity.vo.search.ExamSubjectSiteSearch;
import com.curator.backend.register.exam.subject.service.ExamSubjectService;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 考试科目 前端控制器
 *
 * @author Jun
 * @since 2021-05-13
 */
@RestController
@RequestMapping("/examSubject")
public class ExamSubjectController {

    @Autowired
    private ExamSubjectService examSubjectService;

    /**
     * 考试科目分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/page")
    @Log(controllerName = "ExamSubjectController", remark = "考试科目分页查询")
    ResultResponse<PageResult<ExamSubjectDTO>> pageWithExamSubject(ExamSubjectSearch search) {
        return examSubjectService.pageWithExamSubject(search);
    }

    /**
     * 考试科目列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/list")
    @Log(controllerName = "ExamSubjectController", remark = "考试科目列表查询")
    ResultResponse<List<ExamSubjectDTO>> listWithExamSubject(ExamSubjectSearch search) {
        return examSubjectService.listWithExamSubject(search);
    }

    /**
     * 查询考试科目
     *
     * @param id 考试科目ID
     * @return {@link ResultResponse}
     */
    @GetMapping("/{id}")
    @Log(controllerName = "ExamSubjectController", remark = "查询考试科目")
    ResultResponse<ExamSubjectDTO> getExamSubject(@PathVariable("id") String id) {
        return examSubjectService.getExamSubject(id);
    }

    /**
     * 添加考试科目
     *
     * @param info 考试科目信息
     * @return {@link ResultResponse}
     */
    @PostMapping
    @Log(controllerName = "ExamSubjectController", remark = "添加考试科目")
    ResultResponse<ExamSubjectDTO> saveExamSubject(@RequestBody ExamSubjectInfo info) {
        return examSubjectService.saveExamSubject(info);
    }

    /**
     * 编辑考试科目
     *
     * @param info 考试科目信息
     * @return {@link ResultResponse}
     */
    @PutMapping
    @Log(controllerName = "ExamSubjectController", remark = "编辑考试科目")
    ResultResponse<ExamSubjectDTO> putExamSubject(@RequestBody ExamSubjectInfo info) {
        return examSubjectService.putExamSubject(info);
    }

    /**
     * 删除考试科目
     *
     * @param id 考试科目ID
     * @return {@link ResultResponse}
     */
    @DeleteMapping("/{id}")
    @Log(controllerName = "ExamSubjectController", remark = "删除考试科目")
    ResultResponse<String> removeExamSubject(@PathVariable("id") String id) {
        return examSubjectService.removeExamSubject(id);
    }

    /**
     * 考试科目下考点分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/examSite/page")
    @Log(controllerName = "ExamSubjectController", remark = "考试科目下考点分页查询")
    ResultResponse<PageResult<ExamSubjectSiteDTO>> pageWithExamSubjectSite(ExamSubjectSiteSearch search) {
        return examSubjectService.pageWithExamSubjectSite(search);
    }

    /**
     * 添加考点至考试科目
     *
     * @param info 关联信息
     * @return
     */
    @PostMapping("/add/examSite")
    @Log(controllerName = "ExamSubjectController", remark = "添加考点至考试科目")
    ResultResponse<?> addExamSiteToSubject(@RequestBody ExamSubjectSiteInfo info) {
        return examSubjectService.addExamSiteToSubject(info);
    }
}
