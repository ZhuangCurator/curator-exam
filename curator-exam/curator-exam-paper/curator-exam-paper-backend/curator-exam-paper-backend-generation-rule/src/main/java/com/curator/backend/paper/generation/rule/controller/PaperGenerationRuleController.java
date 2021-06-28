package com.curator.backend.paper.generation.rule.controller;

import com.curator.backend.paper.generation.rule.entity.dto.PaperGenerationRuleDTO;
import com.curator.backend.paper.generation.rule.entity.vo.info.PaperGenerationRuleInfo;
import com.curator.backend.paper.generation.rule.entity.vo.search.PaperGenerationRuleSearch;
import com.curator.backend.paper.generation.rule.service.PaperGenerationRuleService;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 试卷生成规则 前端控制器
 *
 * @author Jun
 * @since 2021-05-08
 */
@RestController
@RequestMapping("/paperGenerationRule")
public class PaperGenerationRuleController {

    @Autowired
    private PaperGenerationRuleService paperGenerationRuleService;

    /**
     * 试卷生成规则分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/page")
    @Log(controllerName = "PaperGenerationRuleController", remark = "试卷生成规则分页查询")
    ResultResponse<PageResult<PaperGenerationRuleDTO>> pageWithPaperGenerationRule(PaperGenerationRuleSearch search) {
        return paperGenerationRuleService.pageWithPaperGenerationRule(search);
    }

    /**
     * 试卷生成规则列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    @GetMapping("/list")
    @Log(controllerName = "PaperGenerationRuleController", remark = "试卷生成规则列表查询")
    ResultResponse<List<PaperGenerationRuleDTO>> listWithPaperGenerationRule(PaperGenerationRuleSearch search) {
        return paperGenerationRuleService.listWithPaperGenerationRule(search);
    }

    /**
     * 查询试卷生成规则
     *
     * @param id 试卷生成规则ID
     * @return {@link ResultResponse}
     */
    @GetMapping("/{id}")
    @Log(controllerName = "PaperGenerationRuleController", remark = "查询试卷生成规则")
    ResultResponse<PaperGenerationRuleDTO> getPaperGenerationRule(@PathVariable("id") String id) {
        return paperGenerationRuleService.getPaperGenerationRule(id);
    }

    /**
     * 添加试卷生成规则
     *
     * @param info 试卷生成规则信息
     * @return {@link ResultResponse}
     */
    @PostMapping
    @Log(controllerName = "PaperGenerationRuleController", remark = "添加试卷生成规则")
    ResultResponse<PaperGenerationRuleDTO> savePaperGenerationRule(@RequestBody PaperGenerationRuleInfo info) {
        return paperGenerationRuleService.savePaperGenerationRule(info);
    }

    /**
     * 编辑试卷生成规则
     *
     * @param info 试卷生成规则信息
     * @return {@link ResultResponse}
     */
    @PutMapping
    @Log(controllerName = "PaperGenerationRuleController", remark = "编辑试卷生成规则")
    ResultResponse<PaperGenerationRuleDTO> putPaperGenerationRule(@RequestBody PaperGenerationRuleInfo info) {
        return paperGenerationRuleService.putPaperGenerationRule(info);
    }

    /**
     * 删除试卷生成规则
     *
     * @param id 试卷生成规则ID
     * @return {@link ResultResponse}
     */
    @DeleteMapping("/{id}")
    @Log(controllerName = "PaperGenerationRuleController", remark = "删除试卷生成规则")
    ResultResponse<String> removePaperGenerationRule(@PathVariable("id") String id) {
        return paperGenerationRuleService.removePaperGenerationRule(id);
    }

}
