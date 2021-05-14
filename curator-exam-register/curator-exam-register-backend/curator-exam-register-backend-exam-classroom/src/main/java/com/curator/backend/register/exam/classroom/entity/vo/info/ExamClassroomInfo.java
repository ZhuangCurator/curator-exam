package com.curator.backend.register.exam.classroom.entity.vo.info;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 教室 页面信息
 *
 * @author Jun
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamClassroomInfo implements Serializable {

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

}
