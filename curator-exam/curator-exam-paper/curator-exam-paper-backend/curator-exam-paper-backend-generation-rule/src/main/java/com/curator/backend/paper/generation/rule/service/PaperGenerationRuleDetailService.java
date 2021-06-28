package com.curator.backend.paper.generation.rule.service;

import com.curator.backend.paper.generation.rule.entity.dto.PaperGenerationRuleDetailDTO;
import com.curator.backend.paper.generation.rule.entity.vo.info.PaperGenerationRuleDetailInfo;
import com.curator.common.support.ResultResponse;

import java.util.List;

/**
 * 试卷生成规则详情 服务类
 *
 * @author Jun
 * @since 2021-05-08
 */
public interface PaperGenerationRuleDetailService {

    /**
     * 试卷生成规则详情列表查询
     *
     * @param generationRuleId 试卷生成规则ID
     * @return {@link ResultResponse}
     */
    ResultResponse<List<PaperGenerationRuleDetailDTO>> listWithPaperGenerationRuleDetail(String generationRuleId);

    /**
     * 查询试卷生成规则详情
     *
     * @param id 试卷生成规则详情ID
     * @return {@link ResultResponse}
     */
    ResultResponse<PaperGenerationRuleDetailDTO> getPaperGenerationRuleDetail(String id);

    /**
     * 添加试卷生成规则详情
     *
     * @param info 试卷生成规则详情信息
     * @return {@link ResultResponse}
     */
    ResultResponse<PaperGenerationRuleDetailDTO> savePaperGenerationRuleDetail(PaperGenerationRuleDetailInfo info);

    /**
     * 编辑试卷生成规则详情
     *
     * @param info 试卷生成规则详情信息
     * @return {@link ResultResponse}
     */
    ResultResponse<PaperGenerationRuleDetailDTO> putPaperGenerationRuleDetail(PaperGenerationRuleDetailInfo info);

    /**
     * 删除试卷生成规则详情
     *
     * @param id 试卷生成规则详情ID
     * @return {@link ResultResponse}
     */
    ResultResponse<String> removePaperGenerationRuleDetail(String id);

}
