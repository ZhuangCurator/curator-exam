package com.curator.backend.paper.test.paper.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 试卷与试题关联
 *
 * @author Jun
 * @since 2021-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TestPaperQuestionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String testPaperQuestionId;

    /**
     * 试卷ID
     */
    private String testPaperId;

    /**
     * 考生报考信息ID
     */
    private String examRegisterInfoId;

    /**
     * 试题ID
     */
    private String questionId;

    /**
     * 题干
     */
    private String questionStem;

    /**
     * 试题答案集合
     */
    private List<String> questionAnswerList;

    /**
     * 试题类型
     */
    private Integer questionType;

    /**
     * 试题序号
     */
    private Integer questionSort;

    /**
     * 该试题考生是否已答(1-是，0-否)
     */
    private Integer handled;

    /**
     * 考生所填答案
     */
    private List<String> userAnswerList;

    /**
     * 考生该题获得分数
     */
    private BigDecimal userPoint;

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
