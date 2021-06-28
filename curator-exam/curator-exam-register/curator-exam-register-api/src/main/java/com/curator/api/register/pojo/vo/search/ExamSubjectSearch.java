package com.curator.api.register.pojo.vo.search;

import com.curator.common.support.BaseSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 考试科目 查询条件
 *
 * @author Jun
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamSubjectSearch extends BaseSearch {

    private static final long serialVersionUID = 1L;

    /**
     * 考试类别ID
     */
    private String examCategoryId;

    /**
     * 考试科目名称
     */
    private String examSubjectName;

}
