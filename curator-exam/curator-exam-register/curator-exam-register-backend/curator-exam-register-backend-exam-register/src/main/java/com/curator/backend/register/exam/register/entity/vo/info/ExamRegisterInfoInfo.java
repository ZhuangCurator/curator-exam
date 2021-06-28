package com.curator.backend.register.exam.register.entity.vo.info;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 考试报名信息 页面信息
 *
 * @author Jun
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamRegisterInfoInfo implements Serializable {

    /**
     * 考试报名信息id
     */
    private String examRegisterInfoId;

    /**
     * 考试类别ID
     */
    private String examCategoryId;

    /**
     * 考试科目ID
     */
    private String examSubjectId;

    /**
     * 考点ID
     */
    private String examSiteId;

    /**
     * 试卷状态(3-考试需新卷重考, 4-考试需原卷重考)
     */
    private Integer paperStatus;
}
