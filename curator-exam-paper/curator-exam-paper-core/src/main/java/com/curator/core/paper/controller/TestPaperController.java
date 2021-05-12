package com.curator.core.paper.controller;

import com.curator.api.paper.pojo.dto.PaperQuestionDTO;
import com.curator.api.paper.pojo.vo.TestPaperInfo;
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
    ResultResponse<PaperQuestionDTO> getPaperQuestion(TestPaperInfo info) {
        return testPaperService.getPaperQuestion(info);
    }


}
