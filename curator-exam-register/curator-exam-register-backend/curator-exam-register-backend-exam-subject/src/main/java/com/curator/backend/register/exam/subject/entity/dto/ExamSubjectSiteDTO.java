package com.curator.backend.register.exam.subject.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 考试科目与考点关联 页面信息
 *
 * @author Jun
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamSubjectSiteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String subjectSiteId;

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
     * 考点名称
     */
    private String examSiteName;

    /**
     * 考点人数限制
     */
    private Integer numberLimit;

    /**
     * 报名人数
     */
    private Integer registerNumber;

    /**
     * 省(代码)
     */
    private String province;

    /**
     * 省(名称)
     */
    private String provinceName;

    /**
     * 市(代码)
     */
    private String city;

    /**
     * 市(名称)
     */
    private String cityName;

    /**
     * 区(代码)
     */
    private String district;

    /**
     * 区(名称)
     */
    private String districtName;

    /**
     * 详细地址
     */
    private String address;

}
