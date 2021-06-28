package com.curator.backend.paper.generation.rule.controller;

import com.curator.backend.paper.generation.rule.entity.dto.PaperGenerationRuleDetailDTO;
import com.curator.backend.paper.generation.rule.entity.vo.info.PaperGenerationRuleDetailInfo;
import com.curator.backend.paper.generation.rule.service.PaperGenerationRuleDetailService;
import com.curator.common.annotation.Log;
import com.curator.common.support.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 试卷生成规则详情 前端控制器
 *
 * @author Jun
 * @since 2021-05-08
 */
@RestController
@RequestMapping("/paperGenerationRuleDetail")
public class PaperGenerationRuleDetailController {

    @Autowired
    private PaperGenerationRuleDetailService generationRuleDetailService;

    /**
     * 试卷生成规则详情列表查询
     *
     * @param generationRuleId 试卷生成规则ID
     * @return {@link ResultResponse}
     */
    @GetMapping("/list/{generationRuleId}")
    @Log(controllerName = "PaperGenerationRuleDetailController", remark = "试卷生成规则详情列表查询")
    ResultResponse<List<PaperGenerationRuleDetailDTO>> listWithPaperGenerationRuleDetail(@PathVariable String generationRuleId) {
        return generationRuleDetailService.listWithPaperGenerationRuleDetail(generationRuleId);
    }

    /**
     * 查询试卷生成规则详情
     *
     * @param id 试卷生成规则详情ID
     * @return {@link ResultResponse}
     */
    @GetMapping("/{id}")
    @Log(controllerName = "PaperGenerationRuleDetailController", remark = "查询试卷生成规则详情")
    ResultResponse<PaperGenerationRuleDetailDTO> getPaperGenerationRuleDetail(@PathVariable("id") String id) {
        return generationRuleDetailService.getPaperGenerationRuleDetail(id);
    }

    /**
     * 添加试卷生成规则详情
     *
     * @param info 试卷生成规则详情信息
     * @return {@link ResultResponse}
     */
    @PostMapping
    @Log(controllerName = "PaperGenerationRuleDetailController", remark = "添加试卷生成规则详情")
    ResultResponse<PaperGenerationRuleDetailDTO> savePaperGenerationRuleDetail(@RequestBody PaperGenerationRuleDetailInfo info) {
        return generationRuleDetailService.savePaperGenerationRuleDetail(info);
    }

    /**
     * 编辑试卷生成规则详情
     *
     * @param info 试卷生成规则详情信息
     * @return {@link ResultResponse}
     */
    @PutMapping
    @Log(controllerName = "PaperGenerationRuleDetailController", remark = "编辑试卷生成规则详情")
    ResultResponse<PaperGenerationRuleDetailDTO> putPaperGenerationRuleDetail(@RequestBody PaperGenerationRuleDetailInfo info) {
        return generationRuleDetailService.putPaperGenerationRuleDetail(info);
    }

    /**
     * 删除试卷生成规则详情
     *
     * @param id 试卷生成规则详情ID
     * @return {@link ResultResponse}
     */
    @DeleteMapping("/{id}")
    @Log(controllerName = "PaperGenerationRuleDetailController", remark = "删除试卷生成规则详情")
    ResultResponse<String> removePaperGenerationRuleDetail(@PathVariable("id") String id) {
        return generationRuleDetailService.removePaperGenerationRuleDetail(id);
    }
}
