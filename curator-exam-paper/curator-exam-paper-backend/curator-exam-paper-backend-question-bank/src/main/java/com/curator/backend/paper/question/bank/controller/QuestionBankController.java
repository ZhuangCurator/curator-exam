package com.curator.backend.paper.question.bank.controller;

import com.curator.backend.paper.question.bank.entity.dto.QuestionBankDTO;
import com.curator.backend.paper.question.bank.entity.vo.info.QuestionBankInfo;
import com.curator.backend.paper.question.bank.entity.vo.seacrh.QuestionBankSearch;
import com.curator.backend.paper.question.bank.service.QuestionBankService;
import com.curator.backend.paper.question.entity.dto.QuestionDTO;
import com.curator.backend.paper.question.entity.vo.search.QuestionSearch;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 试题库库 前端控制器
 *
 * @author Jun
 * @since 2021-05-08
 */
@Controller
@RequestMapping("/questionBank")
public class QuestionBankController {

    @Autowired
    private QuestionBankService questionBankService;

    /**
     * 试题库分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/page")
    @Log(controllerName = "QuestionBankController", remark = "试题库分页查询")
    ResultResponse<PageResult<QuestionBankDTO>> pageWithQuestionBank(QuestionBankSearch search) {
        return questionBankService.pageWithQuestionBank(search);
    }

    /**
     * 试题库列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/list")
    @Log(controllerName = "QuestionBankController", remark = "试题库列表查询")
    ResultResponse<List<QuestionBankDTO>> listWithQuestionBank(QuestionBankSearch search) {
        return questionBankService.listWithQuestionBank(search);
    }

    /**
     * 查询试题库
     *
     * @param id 试题库ID
     * @return {@link ResultResponse}
     */
    @GetMapping("/{id}")
    @Log(controllerName = "QuestionBankController", remark = "查询试题库")
    ResultResponse<QuestionBankDTO> getQuestionBank(@PathVariable("id") String id) {
        return questionBankService.getQuestionBank(id);
    }

    /**
     * 添加试题库
     *
     * @param info 试题库信息
     * @return {@link ResultResponse}
     */
    @PostMapping
    @Log(controllerName = "QuestionBankController", remark = "添加试题库")
    ResultResponse<QuestionBankDTO> saveQuestionBank(@RequestBody QuestionBankInfo info) {
        return questionBankService.saveQuestionBank(info);
    }

    /**
     * 编辑试题库
     *
     * @param info 试题库信息
     * @return {@link ResultResponse}
     */
    @PutMapping
    @Log(controllerName = "QuestionBankController", remark = "编辑试题库")
    ResultResponse<QuestionBankDTO> putQuestionBank(@RequestBody QuestionBankInfo info) {
        return questionBankService.putQuestionBank(info);
    }

    /**
     * 删除试题库
     *
     * @param id 试题库ID
     * @return {@link ResultResponse}
     */
    @DeleteMapping("/{id}")
    @Log(controllerName = "QuestionBankController", remark = "删除试题库")
    ResultResponse<String> removeQuestionBank(@PathVariable("id") String id) {
        return questionBankService.removeQuestionBank(id);
    }

    /**
     * 试题库添加试题
     *
     * @param info 试题库信息
     * @return {@link ResultResponse}
     */
    @PostMapping("/bind/question")
    @Log(controllerName = "QuestionBankController", remark = "试题库添加试题")
    ResultResponse<?> bindQuestionToQuestionBank(@RequestBody QuestionBankInfo info) {
        return questionBankService.bindQuestionToQuestionBank(info);
    }

    /**
     * 试题库中试题分页查询
     *
     * @param search 试题分页查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/page/question")
    @Log(controllerName = "QuestionBankController", remark = "试题库中试题分页查询")
    ResultResponse<PageResult<QuestionDTO>> pageWithQuestion(QuestionSearch search) {
        return questionBankService.pageWithQuestion(search);
    }

    /**
     * 试题库删除试题
     *
     * @param info 试题库信息
     * @return {@link ResultResponse}
     */
    @PostMapping("/remove/question")
    @Log(controllerName = "QuestionBankController", remark = "试题库删除试题")
    ResultResponse<?> removeQuestionFromQuestionBank(@RequestBody QuestionBankInfo info) {
        return questionBankService.removeQuestionFromQuestionBank(info);
    }
}
