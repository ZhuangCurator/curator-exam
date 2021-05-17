package com.curator.core.register.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.curator.api.register.pojo.dto.ExamSubjectDTO;
import com.curator.api.register.pojo.dto.ExamSubjectDTO;
import com.curator.api.register.pojo.vo.search.ExamSubjectSearch;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.core.register.entity.ExamRegisterInfo;
import com.curator.core.register.entity.ExamSubject;
import com.curator.core.register.mapper.ExamRegisterInfoMapper;
import com.curator.core.register.mapper.ExamSubjectMapper;
import com.curator.core.register.service.ExamSubjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 考试科目 服务类
 *
 * @author Jun
 * @date 2021/5/17
 */
@Service
public class ExamSubjectServiceImpl implements ExamSubjectService {

    @Autowired
    private ExamSubjectMapper examSubjectMapper;
    @Autowired
    private ExamRegisterInfoMapper registerInfoMapper;
    
    @Override
    public ResultResponse<List<ExamSubjectDTO>> listWithExamSubject(ExamSubjectSearch search) {
        // 查询考试科目
        QueryWrapper<ExamSubject> subjectWrapper = new QueryWrapper<>();
        subjectWrapper.eq("exam_category_id", search.getExamCategoryId())
                .orderByDesc("create_time");
        List<ExamSubject> subjectList = examSubjectMapper.selectList(subjectWrapper);
        if(Help.isEmpty(subjectList)) {
            return ResultResponse.<List<ExamSubjectDTO>>builder().failure("当前考试类别下不存在考试科目!").build();
        }
        // 过滤出报名未结束的考试科目
        List<ExamSubject> filterList = subjectList.stream()
                .filter(subject -> subject.getRegisterEndTime().isAfter(LocalDateTime.now())).collect(Collectors.toList());
        if(Help.isEmpty(filterList)) {
            return ResultResponse.<List<ExamSubjectDTO>>builder().failure("当前考试类别下所有考试科目报名时间已结束!").build();
        }
        List<ExamSubjectDTO> resultList = filterList.stream().map(this::convertEntity).collect(Collectors.toList());
        return ResultResponse.<List<ExamSubjectDTO>>builder().success("考试科目查询成功!").data(resultList).build();
    }

    /**
     * 将 数据库对象 转为 数据传输对象
     *
     * @param entity 数据库信息
     * @return dto实体类
     */
    private ExamSubjectDTO convertEntity(ExamSubject entity) {
        ExamSubjectDTO target = new ExamSubjectDTO();
        if (Help.isNotEmpty(entity)) {
            BeanUtils.copyProperties(entity, target);
            target.setRegisterNumber(0);
            QueryWrapper<ExamRegisterInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("exam_subject_id", entity.getExamSubjectId());
            Integer count = registerInfoMapper.selectCount(wrapper);
            if(count != null) {
                target.setRegisterNumber(count);
            }

        }
        return target;
    }
}
