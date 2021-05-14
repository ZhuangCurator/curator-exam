package com.curator.backend.register.exam.subject.entity.vo.info;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 考试科目 页面信息
 *
 * @author Jun
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamSubjectInfo implements Serializable {

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

}
