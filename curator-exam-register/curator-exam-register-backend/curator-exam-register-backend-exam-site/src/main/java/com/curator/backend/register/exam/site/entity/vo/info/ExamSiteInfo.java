package com.curator.backend.register.exam.site.entity.vo.info;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 考点 页面信息
 *
 * @author Jun
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamSiteInfo implements Serializable {

    private static final long serialVersionUID = 1L;

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
