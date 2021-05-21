package com.curator.backend.register.exam.classroom.mapper;

import com.curator.backend.register.exam.classroom.entity.ExamClassroom;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 教室 Mapper 接口
 *
 * @author Jun
 * @since 2021-05-13
 */
public interface ExamClassroomMapper extends BaseMapper<ExamClassroom> {

    /**
     * 查询当前考点下教室的最大序列号
     *
     * @param examSiteId 考点ID
     * @return
     */
    Integer selectMaxSerialNum(@Param("examSiteId") String examSiteId);
}
