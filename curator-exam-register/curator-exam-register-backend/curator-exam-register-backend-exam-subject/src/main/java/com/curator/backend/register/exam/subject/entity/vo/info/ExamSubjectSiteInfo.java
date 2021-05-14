package com.curator.backend.register.exam.subject.entity.vo.info;

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
public class ExamSubjectSiteInfo implements Serializable {

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

    /**
     * 考点ID
     */
    private List<String> examSiteIdList;

}
