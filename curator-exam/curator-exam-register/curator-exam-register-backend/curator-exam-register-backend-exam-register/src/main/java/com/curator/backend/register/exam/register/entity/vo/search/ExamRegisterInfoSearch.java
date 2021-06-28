package com.curator.backend.register.exam.register.entity.vo.search;

import com.curator.common.support.BaseSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 考试报名信息 分页查询条件
 *
 * @author Jun
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamRegisterInfoSearch extends BaseSearch {

    private static final long serialVersionUID = 1L;

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
     * 考生账户名称
     */
    private String accountName;

    /**
     * 考生身份证号
     */
    private String idCard;

    /**
     * 准考证编号
     */
    private String admissionNumber;

    /**
     * 座位号
     */
    private Integer seatNumber;

    /**
     * 考试状态(0:未开始考试,1:已结束考试,2:缺考)
     */
    private Integer examStatus;

    /**
     * 是否及格（1-是，0-否）
     */
    private Integer passed;

}
