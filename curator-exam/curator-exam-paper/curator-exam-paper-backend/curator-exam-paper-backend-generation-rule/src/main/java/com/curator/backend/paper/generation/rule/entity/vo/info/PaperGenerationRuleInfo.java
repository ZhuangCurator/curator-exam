package com.curator.backend.paper.generation.rule.entity.vo.info;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 试卷生成规则 页面信息
 *
 * @author Jun
 * @since 2021-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PaperGenerationRuleInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String generationRuleId;

    /**
     * 试题库ID
     */
    private String questionBankId;

    /**
     * 规则名称
     */
    private String generationRuleName;

    /**
     * 规则简介
     */
    private String summary;

}
