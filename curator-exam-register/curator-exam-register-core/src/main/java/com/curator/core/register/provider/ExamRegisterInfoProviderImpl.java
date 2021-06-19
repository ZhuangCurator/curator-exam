package com.curator.core.register.provider;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.curator.api.register.enums.ExamStatusTypeEnum;
import com.curator.api.register.pojo.dto.ExamRegisterInfoDTO;
import com.curator.api.register.provider.ExamRegisterInfoProvider;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.core.register.entity.ExamRegisterInfo;
import com.curator.core.register.entity.ExamSubject;
import com.curator.core.register.mapper.ExamRegisterInfoMapper;
import com.curator.core.register.mapper.ExamSubjectMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.text.TabableView;
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
    public ResultResponse<ExamRegisterInfoDTO> accountLogin(String accountName, String admissionNumber) {
        QueryWrapper<ExamRegisterInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("account_name", accountName)
                .eq("admission_number",admissionNumber);
        ExamRegisterInfo entity = registerInfoMapper.selectOne(wrapper);
        if(Help.isEmpty(entity)) {
            return ResultResponse.<ExamRegisterInfoDTO>builder().failure("不存在该考生报名信息,请检查输入的账户名称和准考证号!").build();
        } else if(entity.getExamStatus() == ExamStatusTypeEnum.ALREADY_OVER.getStatus()){
            return ResultResponse.<ExamRegisterInfoDTO>builder().failure("该科目考试您已完成,不允许在进行考试!").build();
        } else if(entity.getExamStatus() == ExamStatusTypeEnum.MISSED_EXAM.getStatus()){
            return ResultResponse.<ExamRegisterInfoDTO>builder().failure("该科目考试您已缺考,不允许在进行考试!").build();
        }
        ExamSubject examSubject = examSubjectMapper.selectById(entity.getExamSubjectId());
        // 科目考试时间可以提前十分钟考试
        LocalDateTime startTime = examSubject.getExamStartTime().minusMinutes(10);
        if(LocalDateTime.now().isBefore(startTime)) {
            return ResultResponse.<ExamRegisterInfoDTO>builder().failure("还未到考试开始时间,请先等待!").build();
        } else if(LocalDateTime.now().isAfter(examSubject.getExamEndTime())) {
            return ResultResponse.<ExamRegisterInfoDTO>builder().failure("科目考试已结束!").build();
        }
        ExamRegisterInfoDTO dto = new ExamRegisterInfoDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setGenerationRuleId(examSubject.getGenerationRuleId());
        return ResultResponse.<ExamRegisterInfoDTO>builder().success("考生登录成功!").data(dto).build();
    }

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
            examRegisterInfo.setExamPassword(RandomUtil.randomNumbers(6));
            // 更新口令
            registerInfoMapper.update(examRegisterInfo, new UpdateWrapper<ExamRegisterInfo>().eq("exam_register_info_id", examRegisterInfoId));
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
