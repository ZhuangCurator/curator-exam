package com.curator.api.register.pojo.vo.search;

import com.curator.common.support.BaseSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 考试公告 查询条件
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

    /**
     * 省(代码)
     */
    private String province;

    /**
     * 市(代码)
     */
    private String city;

}
