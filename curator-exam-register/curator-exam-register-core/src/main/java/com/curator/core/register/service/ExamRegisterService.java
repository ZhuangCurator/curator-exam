package com.curator.core.register.service;

import com.curator.api.register.pojo.dto.ExamRegisterInfoDTO;
import com.curator.api.register.pojo.vo.info.ExamRegisterInfoInfo;
import com.curator.api.register.pojo.vo.search.ExamRegisterInfoSearch;
import com.curator.common.annotation.Log;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    /**
     * 个人报名信息
     *
     * @param search 分页条件
     * @return
     */
    ResultResponse<PageResult<ExamRegisterInfoDTO>> pageWithRegisterInfo(ExamRegisterInfoSearch search);
}
