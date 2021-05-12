package com.curator.core.paper.service;

import com.curator.api.paper.pojo.dto.PaperQuestionDTO;
import com.curator.api.paper.pojo.vo.TestPaperInfo;
import com.curator.common.annotation.Log;
import com.curator.common.support.ResultResponse;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    /**
     * 保存用户答案
     *
     * @param info 试卷信息
     * @return
     */
    ResultResponse<String> saveUserAnswer(TestPaperInfo info);

    /**
     * 阅卷
     * @param info 试卷信息
     * @return
     */
    ResultResponse<String> markTestPaper(TestPaperInfo info);

}
