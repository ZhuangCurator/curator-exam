package com.curator.backend.paper.test.paper.service;

import com.curator.backend.paper.test.paper.entity.dto.TestPaperDTO;
import com.curator.backend.paper.test.paper.entity.dto.TestPaperQuestionDTO;
import com.curator.backend.paper.test.paper.entity.vo.search.TestPaperSearch;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;

import java.util.List;

/**
 * 考生试卷 服务类
 *
 * @author Jun
 * @since 2021-05-12
 */
public interface TestPaperService {

    /**
     * 考生试卷分页查询
     *
     * @param search 查询条件
     * @return {@link ResultResponse}
     */
    ResultResponse<PageResult<TestPaperDTO>> pageWithTestPaper(TestPaperSearch search);

    /**
     * 考生试卷试题列表查询
     *
     * @param testPaperId 试卷id
     * @return {@link ResultResponse}
     */
    ResultResponse<List<TestPaperQuestionDTO>> listQuestionWithTestPaper(String testPaperId);

}
