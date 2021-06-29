package com.curator.core.register.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 教室
 *
 * @author Jun
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamClassroom implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 教室ID
     */
    @TableId(value = "exam_classroom_id", type = IdType.ASSIGN_ID)
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
     * 创建账户父账户 id
     */
    private String parentAccountId;

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
