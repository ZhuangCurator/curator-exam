package com.curator.backend.register.exam.classroom.entity.vo.search;

import com.curator.common.support.BaseSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 教室 分页查询条件
 *
 * @author Jun
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamClassroomSearch extends BaseSearch {

    private static final long serialVersionUID = 1L;

    /**
     * 考点ID
     */
    private String examSiteId;

    /**
     * 教室名称
     */
    private String examClassroomName;

}
