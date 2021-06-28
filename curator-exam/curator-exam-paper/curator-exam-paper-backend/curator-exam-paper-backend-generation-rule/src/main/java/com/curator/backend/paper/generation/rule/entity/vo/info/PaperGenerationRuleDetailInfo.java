package com.curator.backend.paper.generation.rule.entity.vo.info;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 试卷生成规则详情 页面信息
 *
 * @author Jun
 * @since 2021-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PaperGenerationRuleDetailInfo implements Serializable {

    private static final long serialVersionUID = 1L;

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

}
