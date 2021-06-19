package com.curator.api.paper.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 试卷中的试题类型和数量
 *
 * @author Jun
 * @date 2021/5/12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PaperQuestionGroupDTO implements Serializable {

    /**
     * 试题类型
     */
    private Integer questionType;

    /**
     * 试题类型描述
     */
    private String questionTypeDesc;

    /**
     * 试题数量
     */
    private Integer questionAmount;

    /**
     * 试卷试题 集合
     */
    private List<TestPaperQuestionDTO> paperQuestionList;
}
