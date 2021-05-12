package com.curator.backend.paper.test.paper.controller;

import com.curator.backend.paper.test.paper.entity.dto.TestPaperDTO;
import com.curator.backend.paper.test.paper.entity.dto.TestPaperQuestionDTO;
import com.curator.backend.paper.test.paper.entity.vo.search.TestPaperSearch;
import com.curator.backend.paper.test.paper.service.TestPaperService;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 考生试卷 前端控制器
 *
 * @author Jun
 * @since 2021-05-12
 */
@RestController
@RequestMapping("/testPaper")
public class TestPaperController {

    @Autowired
    private TestPaperService testPaperService;

    /**
     * 考生试卷分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/page")
    @Log(controllerName = "TestPaperController", remark = "考生试卷分页查询")
    ResultResponse<PageResult<TestPaperDTO>> pageWithTestPaper(TestPaperSearch search) {
        return testPaperService.pageWithTestPaper(search);
    }

    /**
     * 考生试卷试题列表查询
     *
     * @param testPaperId 试卷id
     * @return {@link ResultResponse}
     */
    @GetMapping("/question/list/{testPaperId}")
    @Log(controllerName = "TestPaperController", remark = "考生试卷试题列表查询")
    ResultResponse<List<TestPaperQuestionDTO>> listQuestionWithTestPaper(@PathVariable String testPaperId) {
        return testPaperService.listQuestionWithTestPaper(testPaperId);
    }
}
