package com.curator.backend.paper.question.controller;

import com.curator.backend.paper.question.entity.dto.QuestionDTO;
import com.curator.backend.paper.question.entity.vo.info.QuestionInfo;
import com.curator.backend.paper.question.entity.vo.search.QuestionSearch;
import com.curator.backend.paper.question.service.QuestionService;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 试题 前端控制器
 *
 * @author Jun
 * @since 2021-05-08
 */
@Controller
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    /**
     * 试题分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/page")
    @Log(controllerName = "QuestionController", remark = "试题分页查询")
    ResultResponse<PageResult<QuestionDTO>> pageWithQuestion(QuestionSearch search) {
        return questionService.pageWithQuestion(search);
    }

    /**
     * 试题列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/list")
    @Log(controllerName = "QuestionController", remark = "试题列表查询")
    ResultResponse<List<QuestionDTO>> listWithQuestion(QuestionSearch search) {
        return questionService.listWithQuestion(search);
    }

    /**
     * 查询试题
     *
     * @param id 试题ID
     * @return {@link ResultResponse}
     */
    @GetMapping("/{id}")
    @Log(controllerName = "QuestionController", remark = "查询试题")
    ResultResponse<QuestionDTO> getQuestion(@PathVariable("id") String id) {
        return questionService.getQuestion(id);
    }

    /**
     * 添加试题
     *
     * @param info 试题信息
     * @return {@link ResultResponse}
     */
    @PostMapping
    @Log(controllerName = "QuestionController", remark = "添加试题")
    ResultResponse<QuestionDTO> saveQuestion(@RequestBody QuestionInfo info) {
        return questionService.saveQuestion(info);
    }

    /**
     * 编辑试题
     *
     * @param info 试题信息
     * @return {@link ResultResponse}
     */
    @PutMapping
    @Log(controllerName = "QuestionController", remark = "编辑试题")
    ResultResponse<QuestionDTO> putQuestion(@RequestBody QuestionInfo info) {
        return questionService.putQuestion(info);
    }

    /**
     * 删除试题
     *
     * @param id 试题ID
     * @return {@link ResultResponse}
     */
    @DeleteMapping("/{id}")
    @Log(controllerName = "QuestionController", remark = "删除试题")
    ResultResponse<String> removeQuestion(@PathVariable("id") String id) {
        return questionService.removeQuestion(id);
    }

}
