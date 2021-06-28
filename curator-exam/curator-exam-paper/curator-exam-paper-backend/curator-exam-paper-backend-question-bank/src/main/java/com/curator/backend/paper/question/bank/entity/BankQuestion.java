package com.curator.backend.paper.question.bank.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 试题库与试题关联表
 *
 * @author Jun
 * @since 2021-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BankQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "bank_question_id", type = IdType.ASSIGN_ID)
    private String bankQuestionId;

    /**
     * 试题库ID
     */
    private String questionBankId;

    /**
     * 试题ID
     */
    private String questionId;

    /**
     * 试题类型(1-单选，2-多选，3-判断，4-填空)
     */
    private Integer questionType;

    /**
     * 试题难度(1-初级，2-中级，3-高级)
     */
    private Integer questionDifficulty;

    /**
     * 单个试题分数
     */
    private Integer questionPoint;

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
