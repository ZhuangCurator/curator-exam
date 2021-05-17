package com.curator.core.register.service.impl;

import com.curator.api.register.pojo.vo.info.ExamRegisterInfoInfo;
import com.curator.common.support.ResultResponse;
import com.curator.core.register.mapper.ExamRegisterInfoMapper;
import com.curator.core.register.service.ExamRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 考生报名信息 服务实现类
 *
 * @author Jun
 * @date 2021/5/17
 */
@Service
public class ExamRegisterServiceImpl implements ExamRegisterService {

    @Autowired
    private ExamRegisterInfoMapper registerInfoMapper;

    @Override
    public ResultResponse<String> accountRegister(ExamRegisterInfoInfo info) {
        return null;
    }
}
