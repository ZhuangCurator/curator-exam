package com.curator.backend.register.exam.subject.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 考试科目
 *
 * @author Jun
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamSubject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 考试科目ID
     */
    @TableId(value = "exam_subject_id", type = IdType.ASSIGN_ID)
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
    @TableLogic
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    private Integer deleted;

    /**
     * 插入时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
