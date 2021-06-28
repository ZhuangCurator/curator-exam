package com.curator.core.register.controller;

import com.curator.api.register.pojo.dto.ExamNoticeDTO;
import com.curator.api.register.pojo.vo.search.ExamNoticeSearch;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.core.register.service.ExamNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 考试公告 前端控制器
 *
 * @author Jun
 * @date 2021/5/17
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
}
