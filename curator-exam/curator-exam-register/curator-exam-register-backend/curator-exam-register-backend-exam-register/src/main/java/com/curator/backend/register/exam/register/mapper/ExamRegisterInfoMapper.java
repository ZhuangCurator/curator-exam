package com.curator.backend.register.exam.register.mapper;

import com.curator.backend.register.exam.register.entity.ExamRegisterInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 考试报名信息 Mapper 接口
 *
 * @author Jun
 * @since 2021-05-13
 */
public interface ExamRegisterInfoMapper extends BaseMapper<ExamRegisterInfo> {

    /**
     * 更新考生的考试口令
     *
     * @param idList 考生信息id
     * @param examPassword 考试口令
     * @param now 更新时间
     * @return
     */
    int batchUpdateExamPassword(@Param("idList") List<String> idList, @Param("examPassword") String examPassword, @Param("now") LocalDateTime now);

    /**
     * 重置考生信息中的 准考证号/座位号/教室ID
     *
     * @param idList 考生信息ID集合
     * @return
     */
    void resetExamRegisterInfo(@Param("idList") List<String> idList);

    /**
     * 给考生分配教室
     *
     * @param list 考生信息
     * @param now 更新时间
     * @return
     */
    int batchAssignClassroom(@Param("list") List<ExamRegisterInfo> list, @Param("now") LocalDateTime now);

    /**
     * 更新考生报名信息中的 考试状态，成绩，是否合格等字段
     * @param examRegisterInfoId 考生信息id
     * @param now 更新时间
     */
    void updateExamStatusAndScoreAndPassed(@Param("examRegisterInfoId") String examRegisterInfoId , @Param("now") LocalDateTime now);
}
