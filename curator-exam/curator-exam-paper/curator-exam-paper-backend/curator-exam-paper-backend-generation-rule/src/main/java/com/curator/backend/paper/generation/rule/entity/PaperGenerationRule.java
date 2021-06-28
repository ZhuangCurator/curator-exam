package com.curator.backend.paper.generation.rule.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 试卷生成规则
 *
 * @author Jun
 * @since 2021-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PaperGenerationRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "generation_rule_id", type = IdType.ASSIGN_ID)
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
     * 创建账户父账户 id
     */
    private String parentAccountId;

    /**
     * 1 表示删除，0 表示未删除
     */
    @TableLogic
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    private Integer deleted;

    /**
     * 插入时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
