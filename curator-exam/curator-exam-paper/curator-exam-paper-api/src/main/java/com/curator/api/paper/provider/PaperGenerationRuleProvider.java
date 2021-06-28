package com.curator.api.paper.provider;

import com.curator.api.paper.pojo.dto.PaperGenerationRuleDTO;
import com.curator.common.support.ResultResponse;

import java.util.List;

/**
 * 试卷生成规则 RPC接口
 *
 * @author Jun
 * @date 2021/5/14
 */
public interface PaperGenerationRuleProvider {

    /**
     * 试卷生成规则列表
     * @param ruleName 规则名称
     * @return
     */
    ResultResponse<List<PaperGenerationRuleDTO>> listWithPaperGenerationRule(String ruleName);
}
