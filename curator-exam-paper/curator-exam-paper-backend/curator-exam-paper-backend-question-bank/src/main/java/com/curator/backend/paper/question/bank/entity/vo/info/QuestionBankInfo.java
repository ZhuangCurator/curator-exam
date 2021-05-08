package com.curator.backend.paper.question.bank.entity.vo.info;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 试题库 页面信息
 *
 * @author Jun
 * @since 2021-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class QuestionBankInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String questionBankId;

    /**
     * 试题库名称
     */
    private String questionBankName;

    /**
     * 试题库简介
     */
    private String summary;

    /**
     * 试题ID集合(供试题库添加试题所用)
     */
    private List<String> questionIdList;
}
