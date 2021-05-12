package com.curator.backend.paper.question.bank.entity.vo.seacrh;

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
public class QuestionBankSearch extends BaseSearch {

    /**
     * 试题库名
     */
    private String questionBankName;

}
