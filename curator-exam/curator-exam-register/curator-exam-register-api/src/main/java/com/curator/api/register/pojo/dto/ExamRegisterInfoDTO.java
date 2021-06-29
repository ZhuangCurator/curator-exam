package com.curator.api.register.pojo.dto;

import com.curator.api.register.enums.ExamStatusTypeEnum;
import com.curator.common.util.Help;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 考生报名信息 数据传输对象
 *
 * @author Jun
 * @date 2021/5/16
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
     * 试卷生成规则ID
     */
    private String generationRuleId;

    /**
     * 考试开始时间
     */
    private LocalDateTime examStartTime;

    /**
     * 考试结束时间
     */
    private LocalDateTime examEndTime;

    /**
     * 考试剩余时间（单位： 毫秒）
     */
    private Long examDuration;

    /**
     * 考生账户ID
     */
    private String accountId;

    /**
     * 账户名称
     */
    private String accountName;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 考试类别
     */
    private String examCategoryId;

    /**
     * 考试类别名称
     */
    private String examCategoryName;

    /**
     * 考试科目
     */
    private String examSubjectId;

    /**
     * 考试科目名称
     */
    private String examSubjectName;

    /**
     * 考点ID
     */
    private String examSiteId;

    /**
     * 考点名称
     */
    private String examSiteName;

    /**
     * 考点地址
     */
    private String examSiteAddress;

    /**
     * 教室ID
     */
    private String examClassroomId;

    /**
     * 教室名称
     */
    private String examClassroomName;

    /**
     *  教室地址
     */
    private String examClassroomAddress;

    /**
     * 座位号
     */
    private Integer seatNumber;

    /**
     * 准考证编号
     */
    private String admissionNumber;

    /**
     * 考试状态
     */
    private Integer examStatus;

    /**
     * 考试状态描述
     */
    private String examStatusDesc;

    /**
     * 考试成绩
     */
    private BigDecimal score;

    public String getExamStatusDesc() {
        if(examStatus != null) {
            return ExamStatusTypeEnum.getDesc(examStatus);
        }
        return "";
    }

    public String getAdmissionNumber() {
        return Help.isNotEmpty(admissionNumber) ? admissionNumber : "未分配准考证号";
    }
}
