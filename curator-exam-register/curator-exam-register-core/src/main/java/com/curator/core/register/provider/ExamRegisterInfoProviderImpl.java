package com.curator.core.register.provider;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.curator.api.register.pojo.dto.ExamRegisterInfoDTO;
import com.curator.api.register.provider.ExamRegisterInfoProvider;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.core.register.entity.ExamRegisterInfo;
import com.curator.core.register.entity.ExamSubject;
import com.curator.core.register.mapper.ExamRegisterInfoMapper;
import com.curator.core.register.mapper.ExamSubjectMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 考试报名信息 RPC接口实现类
 *
 * @author Jun
 * @date 2021/5/16
 */
@DubboService
public class ExamRegisterInfoProviderImpl implements ExamRegisterInfoProvider {

    @Autowired
    private ExamRegisterInfoMapper registerInfoMapper;
    @Autowired
    private ExamSubjectMapper examSubjectMapper;

    @Override
    public ResultResponse<ExamRegisterInfoDTO> checkExamPassword(String examRegisterInfoId, String examPassword) {
        ExamRegisterInfo examRegisterInfo = registerInfoMapper.selectById(examRegisterInfoId);
        // 若口令相等 计算考试开始时间和结束时间
        if(Help.isNotEmpty(examRegisterInfo.getExamPassword()) && examPassword.equals(examRegisterInfo.getExamPassword())) {
            ExamRegisterInfoDTO dto = new ExamRegisterInfoDTO();
            dto.setExamRegisterInfoId(examRegisterInfoId);
            dto.setExamStartTime(LocalDateTime.now());
            ExamSubject examSubject = examSubjectMapper.selectById(examRegisterInfo.getExamSubjectId());
            // 若检查时间在考试科目开考时间之前
            if(dto.getExamStartTime().isBefore(examSubject.getExamStartTime())) {
                dto.setExamDuration((long) (examSubject.getExamDuration() * 60 * 1000));
                dto.setExamEndTime(dto.getExamStartTime().plusMinutes(examSubject.getExamDuration()));
            } else {
                LocalDateTime examEndTime = dto.getExamStartTime().plusMinutes(examSubject.getExamDuration());
                // 若检查时间在考试科目开考时间之后且计算出的结束时间在考试科目结束时间之前
                if(examEndTime.isBefore(examSubject.getExamEndTime())) {
                    dto.setExamEndTime(examEndTime);
                    dto.setExamDuration((long) (examSubject.getExamDuration() * 60 * 1000));
                } else {
                    // 计算出的结束时间在考试科目结束时间之后
                    dto.setExamEndTime(examSubject.getExamEndTime());
                    dto.setExamDuration(Duration.between(dto.getExamStartTime(), dto.getExamEndTime()).toMillis());
                }
            }
            return ResultResponse.<ExamRegisterInfoDTO>builder().success("考试口令检查成功！").data(dto).build();
        }
        return ResultResponse.<ExamRegisterInfoDTO>builder().failure("考试口令检查失败！").build();
    }

    @Override
    public ResultResponse<?> synchronizeScore(String examRegisterInfoId, BigDecimal score) {
        ExamRegisterInfo examRegisterInfo = registerInfoMapper.selectById(examRegisterInfoId);
        ExamSubject examSubject = examSubjectMapper.selectById(examRegisterInfo.getExamSubjectId());
        if(score.compareTo(new BigDecimal(examSubject.getPassingScore())) >= 0) {
            // 及格了
            examRegisterInfo.setPassed(1);
        }
        examRegisterInfo.setScore(score);
        registerInfoMapper.update(examRegisterInfo, new UpdateWrapper<ExamRegisterInfo>().eq("exam_register_info_id", examRegisterInfoId));
        return ResultResponse.builder().success("成绩同步成功了！").build();
    }
}
