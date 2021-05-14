package com.curator.backend.register.exam.site.entity.vo.search;

import com.curator.common.support.BaseSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 考点 分页查询条件
 *
 * @author Jun
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamSiteSearch extends BaseSearch {

    private static final long serialVersionUID = 1L;

    /**
     * 考点名称
     */
    private String examSiteName;

    /**
     * 省(代码)
     */
    private String province;

    /**
     * 市(代码)
     */
    private String city;

    /**
     * 区(代码)
     */
    private String district;

}
