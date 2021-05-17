package com.curator.api.register.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
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
}
