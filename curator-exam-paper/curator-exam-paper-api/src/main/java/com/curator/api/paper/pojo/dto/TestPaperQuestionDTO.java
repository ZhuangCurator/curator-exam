package com.curator.api.paper.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 试卷与试题关联对象
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
     * 考生所填答案(多个答案用字符串$:$相隔)
     */
    private String userAnswer;

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
