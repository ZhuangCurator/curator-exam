package com.curator.backend.register.exam.notice.entity.vo.info;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 公告
 *
 * @author Jun
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamNoticeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 公告ID
     */
    private String examNoticeId;

    /**
     * 公告名称
     */
    private String examNoticeName;

    /**
     * 内容
     */
    private String content;

}
