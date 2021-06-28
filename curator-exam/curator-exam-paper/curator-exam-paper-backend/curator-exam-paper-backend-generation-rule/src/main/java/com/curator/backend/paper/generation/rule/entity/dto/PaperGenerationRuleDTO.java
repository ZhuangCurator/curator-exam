package com.curator.backend.paper.generation.rule.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 试卷生成规则 数据传输对象
 *
 * @author Jun
 * @since 2021-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PaperGenerationRuleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String generationRuleId;

    /**
     * 试题库ID
     */
    private String questionBankId;

    /**
     * 试题库名称
     */
    private String questionBankName;

    /**
     * 规则名称
     */
    private String generationRuleName;

    /**
     * 生成的试卷总分
     */
    private Integer testPaperPoint;

    /**
     * 规则简介
     */
    private String summary;

    /**
     * 创建账户 id
     */
    private String createAccountId;

    /**
     * 创建账户名称
     */
    private String createAccountName;

    /**
     * 创建账户父账户 id
     */
    private String parentAccountId;

    /**
     * 1 表示删除，0 表示未删除
     */
    private Integer deleted;

    /**
     * 插入时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
