package com.curator.backend.paper.question.entity.dto;

import com.curator.api.paper.enums.QuestionDifficultyEnum;
import com.curator.api.paper.enums.QuestionTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 试题 数据传输对象
 *
 * @author Jun
 * @since 2021-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class QuestionDTO implements Serializable {

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
     * 试题难度描述
     */
    private String questionDifficultyDesc;

    /**
     * 试题类型(1-单选，2-多选，3-判断题, 4-填空)
     */
    private Integer questionType;

    /**
     * 试题类型描述
     */
    private String questionTypeDesc;

    /**
     * 该题分数
     */
    private Integer questionPoint;

    /**
     * 答案是否有序(1-是，0-否)
     */
    private Integer ordered;

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

    /**
     * 答案选项内容
     */
    private List<String> questionAnswerContentList;

    /**
     * 正确答案
     */
    private List<String> questionAnswerRightedList;

    /**
     * 试题答案集合
     */
    private List<QuestionAnswerDTO> questionAnswerDTOList;

    public String getQuestionDifficultyDesc() {
        return QuestionDifficultyEnum.getDesc(questionDifficulty);
    }

    public String getQuestionTypeDesc() {
        return QuestionTypeEnum.getDesc(questionType);
    }
}
