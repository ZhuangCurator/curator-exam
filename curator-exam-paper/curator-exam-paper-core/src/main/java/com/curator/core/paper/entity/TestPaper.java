package com.curator.core.paper.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 考生试卷
 *
 * @author Jun
 * @since 2021-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TestPaper implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "test_paper_id", type = IdType.ASSIGN_ID)
    private String testPaperId;

    /**
     * 考生报考信息ID
     */
    private String examRegisterInfoId;

    /**
     * 试卷总分
     */
    private Integer totalPoint;

    /**
     * 考试成绩
     */
    private BigDecimal examPoint;

    /**
     * 考试状态(1-试卷未开考,2-考试进行中,3-考试需新卷重考, 4-考试需原卷重考, 5-试卷已废弃 ,6-考试已结束 )
     */
    private Integer paperStatus;

    /**
     * 试卷开考时间
     */
    private LocalDateTime paperStartTime;

    /**
     * 试卷结束时间
     */
    private LocalDateTime paperEndTime;

    /**
     * 考生交卷时间
     */
    private LocalDateTime handInTime;

    /**
     * 交卷原因
     */
    private Integer handInReason;

    /**
     * 1 表示删除，0 表示未删除
     */
    @TableLogic
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    private Integer deleted;

    /**
     * 插入时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
