package com.curator.core.paper.service;

import com.curator.api.paper.pojo.dto.PaperQuestionDTO;
import com.curator.api.paper.pojo.vo.TestPaperInfo;
import com.curator.common.support.ResultResponse;

/**
 * 考生试卷 服务类
 *
 * @author Jun
 * @since 2021-05-12
 */
public interface TestPaperService {

    /**
     * 考生初始化试卷
     *
     * @param info 试卷信息
     * @return 考生试卷ID
     */
    ResultResponse<String> initTestPaper(TestPaperInfo info);

    /**
     *  根据序号查询试题
     *
     * @param info 试卷信息
     * @return
     */
    ResultResponse<PaperQuestionDTO> getPaperQuestion(TestPaperInfo info);

}
