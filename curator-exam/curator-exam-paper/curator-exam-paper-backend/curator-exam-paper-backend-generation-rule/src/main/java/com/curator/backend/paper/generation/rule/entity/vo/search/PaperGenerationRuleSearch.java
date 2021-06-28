package com.curator.backend.paper.generation.rule.entity.vo.search;

import com.curator.common.support.BaseSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 试卷生成规则分页查询条件
 *
 * @author Jun
 * @date 2021/5/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PaperGenerationRuleSearch extends BaseSearch {

    /**
     * 规则名称
     */
    private String generationRuleName;
}
