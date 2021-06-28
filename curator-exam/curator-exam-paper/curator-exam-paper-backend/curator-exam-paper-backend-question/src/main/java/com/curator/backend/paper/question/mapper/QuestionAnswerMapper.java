package com.curator.backend.paper.question.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.curator.backend.paper.question.entity.Question;
import com.curator.backend.paper.question.entity.QuestionAnswer;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 试题答案 Mapper 接口
 *
 * @author Jun
 * @since 2021-05-08
 */
public interface QuestionAnswerMapper extends BaseMapper<QuestionAnswer> {

    /**
     * 批量插入试题答案
     *
     * @param list 试题答案集合
     * @return
     */
    int batchAddQuestionAnswer(@Param("list") List<QuestionAnswer> list);

}
