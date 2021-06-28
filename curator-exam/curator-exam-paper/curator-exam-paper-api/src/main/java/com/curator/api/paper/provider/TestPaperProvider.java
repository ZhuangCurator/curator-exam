package com.curator.api.paper.provider;

import com.curator.common.support.ResultResponse;

/**
 * 试卷 RPC接口
 *
 * @author Jun
 * @date 2021/5/14
 */
public interface TestPaperProvider {

    /**
     * 重考
     *
     * @param examRegisterInfoId 考生报名信息id
     * @param paperStatus 试卷状态
     * @return 试卷id
     */
    ResultResponse<String> reExam(String examRegisterInfoId, Integer paperStatus);

    /**
     * 查询此次报名所做的试卷
     *
     * @param examRegisterInfoId 考生报名信息id
     * @return
     */
    ResultResponse<String> selectTestPaper(String examRegisterInfoId);
}
