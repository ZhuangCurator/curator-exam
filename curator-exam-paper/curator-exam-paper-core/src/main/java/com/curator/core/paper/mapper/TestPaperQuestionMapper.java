package com.curator.core.paper.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.curator.core.paper.entity.TestPaperQuestion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 试卷与试题关联 Mapper 接口
 *
 * @author Jun
 * @since 2021-05-12
 */
public interface TestPaperQuestionMapper extends BaseMapper<TestPaperQuestion> {

    /**
     * 批量插入试卷中的试题
     *
     * @param list 试卷中试题集合
     */
    void batchAddPaperQuestion(@Param("list") List<TestPaperQuestion> list);
}
