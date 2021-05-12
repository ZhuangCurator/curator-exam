package com.curator.core.paper.controller;

import com.curator.api.paper.pojo.dto.PaperQuestionDTO;
import com.curator.api.paper.pojo.vo.TestPaperInfo;
import com.curator.common.annotation.Log;
import com.curator.common.support.ResultResponse;
import com.curator.core.paper.service.TestPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 考生试卷 前端核心控制器
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
     * 考生初始化试卷
     *
     *@param info 试卷信息
     * @return 考生试卷ID
     */
    @PostMapping("/init")
    @Log(controllerName = "TestPaperController", remark = "考生初始化试卷")
    ResultResponse<String> initTestPaper(@RequestBody TestPaperInfo info) {
        return testPaperService.initTestPaper(info);
    }

    /**
     *  根据序号查询试题
     *
     * @param info 试卷信息
     * @return 试题数据传输对象
     */
    @GetMapping("/single/question")
    @Log(controllerName = "TestPaperController", remark = "根据序号查询试题")
    ResultResponse<PaperQuestionDTO> getPaperQuestion(TestPaperInfo info) {
        return testPaperService.getPaperQuestion(info);
    }

    /**
     * 保存用户答案
     *
     * @param info 试卷信息
     * @return
     */
    @PutMapping("/save/userAnswer")
    @Log(controllerName = "TestPaperController", remark = "保存用户答案")
    ResultResponse<String> saveUserAnswer(@RequestBody TestPaperInfo info) {
        return testPaperService.saveUserAnswer(info);
    }

    /**
     * 阅卷
     * @param info 试卷信息
     * @return
     */
    @PutMapping("/mark")
    @Log(controllerName = "TestPaperController", remark = "阅卷")
    ResultResponse<String> markTestPaper(@RequestBody TestPaperInfo info) {
        return testPaperService.markTestPaper(info);
    }

}
