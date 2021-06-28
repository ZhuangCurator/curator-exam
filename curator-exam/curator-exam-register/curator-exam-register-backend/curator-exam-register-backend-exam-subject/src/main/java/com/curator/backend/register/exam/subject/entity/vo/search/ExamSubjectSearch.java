package com.curator.backend.register.exam.subject.entity.vo.search;

import com.curator.common.support.BaseSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 考试科目 分页查询信息
 *
 * @author Jun
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamSubjectSearch extends BaseSearch {

    private static final long serialVersionUID = 1L;

    /**
     * 考试科目名称
     */
    private String examSubjectName;

    /**
     * 考试类别ID
     */
    private String examCategoryId;

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

}
