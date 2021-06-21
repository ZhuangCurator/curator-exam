package com.curator.backend.register.exam.notice.entity.dto;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 公告
 *
 * @author Jun
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamNoticeDTO implements Serializable {

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

    /**
     * 省(代码)
     */
    private String province;

    /**
     * 市(代码)
     */
    private String city;

    /**
     * 创建账户 id
     */
    private String createAccountId;

    /**
     * 创建账户名称
     */
    private String createAccountName;

    /**
     * 创建账户父账户 id
     */
    private String parentAccountId;

    /**
     * 1 表示删除，0 表示未删除
     */
    private Integer deleted;

    /**
     * 插入时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
