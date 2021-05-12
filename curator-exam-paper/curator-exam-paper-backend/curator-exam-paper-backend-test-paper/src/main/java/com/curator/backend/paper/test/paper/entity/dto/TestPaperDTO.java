package com.curator.backend.paper.test.paper.entity.dto;

import com.curator.api.paper.enums.TestPaperStatusEnum;
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
public class TestPaperDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String testPaperId;

    /**
     * 考生报考信息ID
     */
    private String examInfoId;

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
     * 试卷状态描述
     */
    private String paperStatusDesc;

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
    private String handInReason;

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

    public String getPaperStatusDesc() {
        return TestPaperStatusEnum.getDesc(paperStatus);
    }
}
