package com.curator.backend.paper.question.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.curator.backend.paper.question.entity.Question;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 试题 Mapper 接口
 *
 * @author Jun
 * @since 2021-05-08
 */
public interface QuestionMapper extends BaseMapper<Question> {

    /**
     * 批量插入试题
     *
     * @param list 试题集合
     * @return
     */
    int batchAddQuestion(@Param("list") List<Question> list);
}
