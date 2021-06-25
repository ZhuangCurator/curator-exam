package com.curator.backend.register.exam.register.entity.dto;

import com.curator.api.register.enums.ExamStatusTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 考试报名信息 数据传输对象
 *
 * @author Jun
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamRegisterInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 考试报名信息id
     */
    private String examRegisterInfoId;

    /**
     * 考试类别ID
     */
    private String examCategoryId;

    /**
     * 考试类别名称
     */
    private String examCategoryName;

    /**
     * 考试科目ID
     */
    private String examSubjectId;

    /**
     * 考试科目名称
     */
    private String examSubjectName;

    /**
     * 试卷生成规则ID
     */
    private String generationRuleId;

    /**
     * 考点ID
     */
    private String examSiteId;

    /**
     * 考点名称
     */
    private String examSiteName;

    /**
     * 教室ID
     */
    private String examClassroomId;

    /**
     * 教室名称
     */
    private String examClassroomName;

    /**
     * 座位号
     */
    private Integer seatNumber;

    /**
     * 考试口令
     */
    private String examPassword;

    /**
     * 考生账户ID
     */
    private String accountId;

    /**
     * 考生账户名称
     */
    private String accountName;

    /**
     * 考生身份证号
     */
    private String idCard;

    /**
     * 考试状态(0:未开始考试,1:已结束考试,2:缺考)
     */
    private Integer examStatus;

    /**
     * 考试状态描述
     */
    private String examStatusDesc;

    /**
     * 准考证编号
     */
    private String admissionNumber;

    /**
     * 开始考试时间(考生见到试卷时间)
     */
    private LocalDateTime startTime;

    /**
     * 考生成绩
     */
    private BigDecimal score;

    /**
     * 是否及格, 1:是 0:否
     */
    private Integer passed;

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

    public String getExamStatusDesc() {
        return ExamStatusTypeEnum.getDesc(examStatus);
    }
}
