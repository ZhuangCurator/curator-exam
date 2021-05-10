package com.curator.backend.paper.question.entity.vo.info;

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
public class QuestionAnswerInfo implements Serializable {

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
    private Integer righted;
}
