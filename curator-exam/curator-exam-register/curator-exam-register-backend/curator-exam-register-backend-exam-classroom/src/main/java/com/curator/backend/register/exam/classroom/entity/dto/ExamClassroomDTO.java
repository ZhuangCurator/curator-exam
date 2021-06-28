package com.curator.backend.register.exam.classroom.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 教室 数据传输对象
 *
 * @author Jun
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamClassroomDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 教室ID
     */
    private String examClassroomId;

    /**
     * 考点ID
     */
    private String examSiteId;

    /**
     * 教室名称
     */
    private String examClassroomName;

    /**
     * 教室人数限制
     */
    private Integer numberLimit;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 创建账户 id
     */
    private String createAccountId;

    /**
     * 创建账户名称
     */
    private String createAccountName;

    /**
     * 创建账户父账户 id
     */
    private String parentAccountId;

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
