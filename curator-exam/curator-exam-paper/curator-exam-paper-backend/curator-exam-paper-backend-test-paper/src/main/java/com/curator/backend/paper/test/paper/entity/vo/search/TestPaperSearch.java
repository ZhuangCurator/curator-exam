package com.curator.backend.paper.test.paper.entity.vo.search;

import com.curator.common.support.BaseSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 试卷查询条件
 *
 * @author Jun
 * @date 2021/5/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TestPaperSearch extends BaseSearch {

    /**
     * 试卷ID
     */
    private String testPaperId;

    /**
     * 考生报考信息ID
     */
    private String examRegisterInfoId;

}
