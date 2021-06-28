package com.curator.backend.paper.question.entity.vo.search;

import com.curator.common.support.BaseSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 试题分页查询条件
 *
 * @author Jun
 * @date 2021/5/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionSearch extends BaseSearch {

    /**
     * 题干
     */
    private String questionStem;

    /**
     * 试题类型
     */
    private Integer questionType;

    /**
     * 试题难度
     */
    private Integer questionDifficulty;

    /**
     * 试题ID集合
     */
    private List<String> questionIdList;

    /**
     * 试题库ID
     */
    private String questionBankId;
}
