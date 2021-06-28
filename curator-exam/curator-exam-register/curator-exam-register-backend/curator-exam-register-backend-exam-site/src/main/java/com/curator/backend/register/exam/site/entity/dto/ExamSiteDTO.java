package com.curator.backend.register.exam.site.entity.dto;

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
