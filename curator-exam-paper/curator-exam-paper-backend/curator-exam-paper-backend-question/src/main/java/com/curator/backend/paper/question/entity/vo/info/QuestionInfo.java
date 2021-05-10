package com.curator.backend.paper.question.entity.vo.info;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 试题 页面数据
 *
 * @author Jun
 * @since 2021-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class QuestionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String questionId;

    /**
     * 题干
     */
    private String questionStem;

    /**
     * 试题解析
     */
    private String questionAnalysis;

    /**
     * 试题难度(1-初级，2-中级，3-高级)
     */
    private Integer questionDifficulty;

    /**
     * 试题类型(1-单选，2-多选，3-判断题, 4-填空)
     */
    private Integer questionType;

    /**
     * 该题分数
     */
    private Integer questionPoint;

    /**
     * 填空题答案是否有序(1-是，0-否)
     */
    private Integer ordered;

    /**
     * 试题答案集合
     */
    private List<QuestionAnswerInfo> questionAnswerInfoList;
}
