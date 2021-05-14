package com.curator.backend.register.exam.register.entity.vo.search;

import com.curator.common.support.BaseSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

}
