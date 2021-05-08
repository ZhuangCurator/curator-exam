package com.curator.backend.paper.generation.rule.service;

import com.curator.backend.paper.generation.rule.entity.dto.PaperGenerationRuleDTO;
import com.curator.backend.paper.generation.rule.entity.vo.info.PaperGenerationRuleInfo;
import com.curator.backend.paper.generation.rule.entity.vo.search.PaperGenerationRuleSearch;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;

import java.util.List;

/**
 * 试卷生成规则 服务类
 *
 * @author Jun
 * @since 2021-05-08
 */
public interface PaperGenerationRuleService {

    /**
     * 试卷生成规则分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<PageResult<PaperGenerationRuleDTO>> pageWithPaperGenerationRule(PaperGenerationRuleSearch search);

    /**
     * 试卷生成规则列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<List<PaperGenerationRuleDTO>> listWithPaperGenerationRule(PaperGenerationRuleSearch search);

    /**
     * 查询试卷生成规则
     *
     * @param id 试卷生成规则ID
     * @return {@link ResultResponse}
     */
    ResultResponse<PaperGenerationRuleDTO> getPaperGenerationRule(String id);

    /**
     * 添加试卷生成规则
     *
     * @param info 试卷生成规则信息
     * @return {@link ResultResponse}
     */
    ResultResponse<PaperGenerationRuleDTO> savePaperGenerationRule(PaperGenerationRuleInfo info);

    /**
     * 编辑试卷生成规则
     *
     * @param info 试卷生成规则信息
     * @return {@link ResultResponse}
     */
    ResultResponse<PaperGenerationRuleDTO> putPaperGenerationRule(PaperGenerationRuleInfo info);

    /**
     * 删除试卷生成规则
     *
     * @param id 试卷生成规则ID
     * @return {@link ResultResponse}
     */
    ResultResponse<String> removePaperGenerationRule(String id);

}
