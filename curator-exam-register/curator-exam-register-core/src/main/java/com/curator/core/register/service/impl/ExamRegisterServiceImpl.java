package com.curator.core.register.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.curator.api.register.enums.ExamStatusTypeEnum;
import com.curator.api.register.pojo.vo.info.ExamRegisterInfoInfo;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.common.util.RedissonUtil;
import com.curator.core.register.entity.ExamCategory;
import com.curator.core.register.entity.ExamRegisterInfo;
import com.curator.core.register.entity.ExamSite;
import com.curator.core.register.entity.ExamSubject;
import com.curator.core.register.mapper.ExamCategoryMapper;
import com.curator.core.register.mapper.ExamRegisterInfoMapper;
import com.curator.core.register.mapper.ExamSiteMapper;
import com.curator.core.register.mapper.ExamSubjectMapper;
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
    @Autowired
    private ExamCategoryMapper examCategoryMapper;
    @Autowired
    private ExamSubjectMapper examSubjectMapper;
    @Autowired
    private ExamSiteMapper examSiteMapper;

    @Override
    public ResultResponse<String> accountRegister(ExamRegisterInfoInfo info) {
        // 判断考试类别/考试科目/考点是否已被删除
        ExamCategory examCategory = examCategoryMapper.selectById(info.getExamCategoryId());
        if(Help.isEmpty(examCategory)) {
            return ResultResponse.<String>builder().failure("该考试类别已不存在!").build();
        }
        ExamSubject examSubject = examSubjectMapper.selectById(info.getExamSubjectId());
        if(Help.isEmpty(examSubject)) {
            return ResultResponse.<String>builder().failure("该考试科目已不存在!").build();
        }
        ExamSite examSite = examSiteMapper.selectById(info.getExamSiteId());
        if(Help.isEmpty(examSite)) {
            return ResultResponse.<String>builder().failure("该考点已不存在!").build();
        }
        // 判断是否已经报名此科目
        QueryWrapper<ExamRegisterInfo> infoWrapper = new QueryWrapper<>();
        infoWrapper.eq("exam_subject_id", info.getExamSubjectId())
                .eq("account_id", info.getAccountId());
        ExamRegisterInfo registerInfo = registerInfoMapper.selectOne(infoWrapper);
        if(Help.isNotEmpty(registerInfo)) {
            return ResultResponse.<String>builder().failure(examSubject.getExamSubjectName() + " 此科目该考生已报名!").build();
        }
        // 考生进行报名
        String lockName = "REGISTER:ACCOUNT";
        RedissonUtil.lock(lockName, 5000);
        try{
            // 检查当前科目考点的报名人数是否超出限制
            infoWrapper = new QueryWrapper<>();
            infoWrapper.eq("exam_subject_id", info.getExamSubjectId())
                    .eq("exam_site_id", info.getExamSiteId());
            Integer count = registerInfoMapper.selectCount(infoWrapper);
            if(count != null && count > examSite.getNumberLimit()) {
                return ResultResponse.<String>builder().failure("当前考点报名人数已满,请选择其他的考点进行报名!").build();
            }
            // 判断是否已经报名此科目
            QueryWrapper<ExamRegisterInfo> registerInfoWrapper = new QueryWrapper<>();
            registerInfoWrapper.eq("account_id", info.getAccountId())
                    .eq("exam_subject_id", info.getExamSubjectId());
            ExamRegisterInfo examRegisterInfo = registerInfoMapper.selectOne(registerInfoWrapper);
            if(Help.isNotEmpty(examRegisterInfo)) {
                return ResultResponse.<String>builder().failure(examSubject.getExamSubjectName() + " 此科目该考生已报名!").build();
            }
            // 添加报名记录
            ExamRegisterInfo entity = new ExamRegisterInfo();
            entity.setExamCategoryId(info.getExamCategoryId());
            entity.setExamSubjectId(info.getExamSubjectId());
            entity.setExamSiteId(info.getExamSiteId());
            entity.setAccountId(info.getAccountId());
            entity.setAccountName(info.getAccountName());
            entity.setExamStatus(ExamStatusTypeEnum.UN_STARTED.getStatus());
            entity.setPassed(0);
            registerInfoMapper.insert(entity);
            return ResultResponse.<String>builder().success("考生报名成功!").data(entity.getExamRegisterInfoId()).build();
        }finally {
            RedissonUtil.unlock(lockName);
        }
    }
}
