package com.curator.backend.register.exam.subject.entity.vo.search;

import com.curator.common.support.BaseSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 考试科目与考点关联 分页查询信息
 *
 * @author Jun
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamSubjectSiteSearch extends BaseSearch {

    private static final long serialVersionUID = 1L;

    /**
     * 考试科目ID
     */
    private String examSubjectId;

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
