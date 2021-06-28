package com.curator.core.register.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.api.register.pojo.dto.ExamCategoryDTO;
import com.curator.api.register.pojo.dto.ExamSubjectDTO;
import com.curator.api.register.pojo.dto.ExamSubjectDTO;
import com.curator.api.register.pojo.vo.search.ExamSubjectSearch;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.core.register.entity.ExamCategory;
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
    public ResultResponse<PageResult<ExamSubjectDTO>> pageWithExamSubject(ExamSubjectSearch search) {
        Page<ExamSubject> page = new Page<>(search.getCurrent(), search.getPageSize());
        // 查询考试科目
        QueryWrapper<ExamSubject> subjectWrapper = new QueryWrapper<>();
        subjectWrapper.eq("exam_category_id", search.getExamCategoryId())
                .like(Help.isNotEmpty(search.getExamSubjectName()), "exam_subject_name", search.getExamSubjectName())
                .orderByDesc("create_time");
        IPage<ExamSubject> iPage = examSubjectMapper.selectPage(page, subjectWrapper);
        if(Help.isEmpty(iPage.getRecords())) {
            return ResultResponse.<PageResult<ExamSubjectDTO>>builder().failure("当前考试类别下不存在考试科目!").build();
        }
        // 过滤出报名未结束的考试科目
        List<ExamSubject> filterList = iPage.getRecords().stream()
                .filter(subject -> subject.getRegisterEndTime().isAfter(LocalDateTime.now())).collect(Collectors.toList());
        if(Help.isEmpty(filterList)) {
            return ResultResponse.<PageResult<ExamSubjectDTO>>builder().failure("当前考试类别下所有考试科目报名时间已结束!").build();
        }
        List<ExamSubjectDTO> resultList = filterList.stream().map(this::convertEntity).collect(Collectors.toList());

        PageResult<ExamSubjectDTO> resultPage = new PageResult<>();
        BeanUtils.copyProperties(iPage, resultPage, "records");
        resultPage.setRecords(resultList);
        return ResultResponse.<PageResult<ExamSubjectDTO>>builder().success("考试科目分页查询成功!").data(resultPage).build();
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
