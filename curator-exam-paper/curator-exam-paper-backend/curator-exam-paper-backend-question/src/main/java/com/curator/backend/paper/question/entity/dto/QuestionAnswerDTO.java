package com.curator.backend.paper.question.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 试题答案 数据传输对象
 *
 * @author Jun
 * @since 2021-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class QuestionAnswerDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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
