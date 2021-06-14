package com.curator.backend.register.exam.notice.entity.vo.search;

import com.curator.common.support.BaseSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 考试公告 分页查询条件
 *
 * @author Jun
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamNoticeSearch extends BaseSearch {

    private static final long serialVersionUID = 1L;

    /**
     * 公告名称
     */
    private String examNoticeName;

}
