package com.curator.backend.register.exam.classroom.service;

import com.curator.backend.register.exam.classroom.entity.dto.ExamClassroomDTO;
import com.curator.backend.register.exam.classroom.entity.vo.info.ExamClassroomInfo;
import com.curator.backend.register.exam.classroom.entity.vo.search.ExamClassroomSearch;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;

import java.util.List;

/**
 * 教室 服务类
 *
 * @author Jun
 * @since 2021-05-13
 */
public interface ExamClassroomService  {

    /**
     * 教室分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<PageResult<ExamClassroomDTO>> pageWithExamClassroom(ExamClassroomSearch search);

    /**
     * 教室列表查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<List<ExamClassroomDTO>> listWithExamClassroom(ExamClassroomSearch search);

    /**
     * 查询教室
     *
     * @param id 教室ID
     * @return {@link ResultResponse}
     */
    ResultResponse<ExamClassroomDTO> getExamClassroom(String id);

    /**
     * 添加教室
     *
     * @param info 教室信息
     * @return {@link ResultResponse}
     */
    ResultResponse<ExamClassroomDTO> saveExamClassroom(ExamClassroomInfo info);

    /**
     * 编辑教室
     *
     * @param info 教室信息
     * @return {@link ResultResponse}
     */
    ResultResponse<ExamClassroomDTO> putExamClassroom(ExamClassroomInfo info);

    /**
     * 删除教室
     *
     * @param id 教室ID
     * @return {@link ResultResponse}
     */
    ResultResponse<String> removeExamClassroom(String id);

}
