package com.curator.backend.paper.question.bank.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 试题库 数据传输对象
 *
 * @author Jun
 * @since 2021-05-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class QuestionBankDTO implements Serializable {

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
     * 创建账户 id
     */
    private String createAccountId;

    /**
     * 创建账户名称
     */
    private String createAccountName;

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

}
