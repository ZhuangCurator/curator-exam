package com.curator.backend.register.exam.register.entity.dto;

import com.baomidou.mybatisplus.annotation.*;
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
     * 考试科目ID
     */
    private String examSubjectId;

    /**
     * 考点ID
     */
    private String examSiteId;

    /**
     * 教室ID
     */
    private String examClassroomId;

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
     * 考试状态(0:未开始考试,1:已结束考试,2:缺考)
     */
    private Integer examStatus;

    /**
     * 准考证url
     */
    private String admissionTicketUrl;

    /**
     * 准考证编号
     */
    private String admissionNumber;

    /**
     * 开始考试时间
     */
    private LocalDateTime startTime;

    /**
     * 考生成绩
     */
    private BigDecimal score;

    /**
     * 是否及格, 1:是 0:否
     */
    private Integer isPassed;

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
