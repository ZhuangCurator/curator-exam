package com.curator.backend.register.exam.subject.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 考试科目 数据传输对象
 *
 * @author Jun
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamSubjectDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 考试科目ID
     */
    private String examSubjectId;

    /**
     * 考试科目名称
     */
    private String examSubjectName;

    /**
     * 考试类别ID
     */
    private String examCategoryId;

    /**
     * 试卷生成规则ID
     */
    private String generationRuleId;

    /**
     * 报名开始时间
     */
    private LocalDateTime registerStartTime;

    /**
     * 报名结束时间
     */
    private LocalDateTime registerEndTime;

    /**
     * 考试开始时间
     */
    private LocalDateTime examStartTime;

    /**
     * 考试结束时间
     */
    private LocalDateTime examEndTime;

    /**
     * 考试时长(单位:分钟,小于考试结束时间与考试开始时间之差)
     */
    private Integer examDuration;

    /**
     * 及格分数线
     */
    private Integer passingScore;

    /**
     * 报名人数
     */
    private Integer registerNumber;

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

}
