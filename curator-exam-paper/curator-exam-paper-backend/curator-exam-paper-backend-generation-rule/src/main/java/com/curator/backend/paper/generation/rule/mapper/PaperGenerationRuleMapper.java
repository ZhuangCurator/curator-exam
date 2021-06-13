package com.curator.backend.paper.generation.rule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.curator.backend.paper.generation.rule.entity.PaperGenerationRule;

/**
 * 试卷生成规则 Mapper 接口
 *
 * @author Jun
 * @since 2021-05-08
 */
public interface PaperGenerationRuleMapper extends BaseMapper<PaperGenerationRule> {

    /**
     * 查询试题库名称
     *
     * @param questionBankId 试题库id
     * @return
     */
    String selectQuestionBankName(String questionBankId);
}
