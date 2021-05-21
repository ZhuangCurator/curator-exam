package com.curator.backend.register.exam.subject.mapper;

import com.curator.backend.register.exam.subject.entity.ExamSubject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 考试科目 Mapper 接口
 *
 * @author Jun
 * @since 2021-05-13
 */
public interface ExamSubjectMapper extends BaseMapper<ExamSubject> {

    /**
     * 查询当前考试类目下考试科目的最大序列号
     *
     * @param examCategoryId 考试类目ID
     * @return
     */
    Integer selectMaxSerialNum(@Param("examCategoryId") String examCategoryId);
}
