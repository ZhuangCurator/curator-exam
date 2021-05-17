package com.curator.api.register.pojo.vo.info;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 考生报名信息 页面信息
 *
 * @author Jun
 * @date 2021/5/16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamRegisterInfoInfo implements Serializable {

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
     * 考生账户ID
     */
    private String accountId;

    /**
     * 考生账户姓名
     */
    private String accountName;
}
