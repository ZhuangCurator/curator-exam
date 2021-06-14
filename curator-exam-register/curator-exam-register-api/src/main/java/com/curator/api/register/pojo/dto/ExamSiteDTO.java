package com.curator.api.register.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 考点 数据传输对象
 *
 * @author Jun
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamSiteDTO implements Serializable {

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
     * 考点名称
     */
    private String examSiteName;

    /**
     * 人数限制
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
     * 考点地址
     */
    private String address;

}
