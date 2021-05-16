package com.curator.api.paper.pojo.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 考生试卷 页面信息
 *
 * @author Jun
 * @since 2021-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TestPaperInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 试卷ID
     */
    private String testPaperId;

    /**
     * 考生报考信息ID
     */
    private String examRegisterInfoId;

    /**
     * 生成试卷规则ID
     */
    private String generationRuleId;

    /**
     * 试题序号
     */
    private String paperQuestionSort;

    /**
     * 试题考生答案
     */
    private String userAnswer;

    /**
     * 交卷原因
     */
    private Integer handInReason;

}
