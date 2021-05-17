package com.curator.core.register.service;

import com.curator.api.register.pojo.vo.info.ExamRegisterInfoInfo;
import com.curator.common.support.ResultResponse;

/**
 * 考生报名信息 服务类
 *
 * @author Jun
 * @date 2021/5/17
 */
public interface ExamRegisterService {

    /**
     * 考生报名
     *
     * @param info 报名信息
     * @return
     */
    ResultResponse<String> accountRegister(ExamRegisterInfoInfo info);
}
