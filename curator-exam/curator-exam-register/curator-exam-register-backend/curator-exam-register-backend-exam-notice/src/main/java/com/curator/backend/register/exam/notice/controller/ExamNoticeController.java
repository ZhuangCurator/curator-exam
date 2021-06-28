package com.curator.backend.register.exam.notice.controller;

import com.curator.backend.register.exam.notice.entity.dto.ExamNoticeDTO;
import com.curator.backend.register.exam.notice.entity.vo.info.ExamNoticeInfo;
import com.curator.backend.register.exam.notice.entity.vo.search.ExamNoticeSearch;
import com.curator.backend.register.exam.notice.service.ExamNoticeService;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 考试公告 前端控制器
 *
 * @author Jun
 * @since 2021-05-13
 */
@RestController
@RequestMapping("/examNotice")
public class ExamNoticeController {

    @Autowired
    private ExamNoticeService examNoticeService;

    /**
     * 考试公告分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/page")
    @Log(controllerName = "ExamNoticeController", remark = "考试公告分页查询")
    ResultResponse<PageResult<ExamNoticeDTO>> pageWithExamNotice(ExamNoticeSearch search) {
        return examNoticeService.pageWithExamNotice(search);
    }

    /**
     * 考试公告列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/list")
    @Log(controllerName = "ExamNoticeController", remark = "考试公告列表查询")
    ResultResponse<List<ExamNoticeDTO>> listWithExamNotice(ExamNoticeSearch search) {
        return examNoticeService.listWithExamNotice(search);
    }

    /**
     * 查询考试公告
     *
     * @param id 考试公告ID
     * @return {@link ResultResponse}
     */
    @GetMapping("/{id}")
    @Log(controllerName = "ExamNoticeController", remark = "查询考试公告")
    ResultResponse<ExamNoticeDTO> getExamNotice(@PathVariable("id") String id) {
        return examNoticeService.getExamNotice(id);
    }

    /**
     * 添加考试公告
     *
     * @param info 考试公告信息
     * @return {@link ResultResponse}
     */
    @PostMapping
    @Log(controllerName = "ExamNoticeController", remark = "添加考试公告")
    ResultResponse<ExamNoticeDTO> saveExamNotice(@RequestBody ExamNoticeInfo info) {
        return examNoticeService.saveExamNotice(info);
    }

    /**
     * 编辑考试公告
     *
     * @param info 考试公告信息
     * @return {@link ResultResponse}
     */
    @PutMapping
    @Log(controllerName = "ExamNoticeController", remark = "编辑考试公告")
    ResultResponse<ExamNoticeDTO> putExamNotice(@RequestBody ExamNoticeInfo info) {
        return examNoticeService.putExamNotice(info);
    }

    /**
     * 删除考试公告
     *
     * @param id 考试公告ID
     * @return {@link ResultResponse}
     */
    @DeleteMapping("/{id}")
    @Log(controllerName = "ExamNoticeController", remark = "删除考试公告")
    ResultResponse<String> removeExamNotice(@PathVariable("id") String id) {
        return examNoticeService.removeExamNotice(id);
    }

}
