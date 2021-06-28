package com.curator.api.register.provider;

import com.curator.api.register.pojo.dto.ExamRegisterInfoDTO;
import com.curator.common.support.ResultResponse;

import java.math.BigDecimal;

/**
 * 考试报名信息 RPC接口
 *
 * @author Jun
 * @date 2021/5/16
 */
public interface ExamRegisterInfoProvider {

    /**
     * 考生账号登录
     *
     * @param accountName 考生账号名
     * @param admissionNumber 准考证编号
     * @return
     */
    ResultResponse<ExamRegisterInfoDTO> accountLogin(String accountName, String admissionNumber);

    /**
     * 校验考试口令
     *
     * @param examRegisterInfoId 考试报名信息id
     * @param examPassword 考试口令
     * @return
     */
    ResultResponse<ExamRegisterInfoDTO> checkExamPassword(String examRegisterInfoId, String examPassword);

    /**
     * 同步考生成绩
     *
     * @param examRegisterInfoId 考试报名信息id
     * @param score 成绩
     * @return
     */
    ResultResponse<?> synchronizeScore(String examRegisterInfoId, BigDecimal score);
}
