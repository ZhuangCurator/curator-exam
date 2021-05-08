package com.curator.backend.paper.question.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 试题答案
 *
 * @author Jun
 * @since 2021-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class QuestionAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "question_answer_id", type = IdType.ASSIGN_ID)
    private String questionAnswerId;

    /**
     * 试题ID
     */
    private String questionId;

    /**
     * 答案内容
     */
    private String content;

    /**
     * 该答案分数
     */
    private BigDecimal questionPoint;

    /**
     * 答案是否正确(1-是，0-否)
     */
    @TableField(value = "is_right")
    private Integer right;

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
