package com.curator.core.paper.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 试卷生成规则详情
 *
 * @author Jun
 * @since 2021-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PaperGenerationRuleDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "generation_rule_detail_id", type = IdType.ASSIGN_ID)
    private String generationRuleDetailId;

    /**
     * 试卷生成规则ID
     */
    private String generationRuleId;

    /**
     * 试题类型(1-单选，2-多选，3-判断，4-填空)
     */
    private Integer questionType;

    /**
     * 试题难度(1-初级，2-中级，3-高级)
     */
    private Integer questionDifficulty;

    /**
     * 试题个数
     */
    private Integer questionNumber;

    /**
     * 单个试题分数
     */
    private Integer questionPoint;

    /**
     * 组卷顺序
     */
    private Integer detailSort;

    /**
     * 简介
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
