package com.curator.backend.register.exam.register.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.api.paper.provider.TestPaperProvider;
import com.curator.api.register.enums.ExamStatusTypeEnum;
import com.curator.backend.register.exam.category.entity.ExamCategory;
import com.curator.backend.register.exam.category.mapper.ExamCategoryMapper;
import com.curator.backend.register.exam.classroom.entity.ExamClassroom;
import com.curator.backend.register.exam.classroom.mapper.ExamClassroomMapper;
import com.curator.backend.register.exam.register.entity.ExamRegisterInfo;
import com.curator.backend.register.exam.register.entity.dto.ExamRegisterInfoDTO;
import com.curator.backend.register.exam.register.entity.dto.ExamRegisterInfoExcelDTO;
import com.curator.backend.register.exam.register.entity.vo.search.ExamRegisterInfoSearch;
import com.curator.backend.register.exam.register.entity.vo.search.info.ExamRegisterInfoInfo;
import com.curator.backend.register.exam.register.mapper.ExamRegisterInfoMapper;
import com.curator.backend.register.exam.register.service.ExamRegisterInfoService;
import com.curator.backend.register.exam.site.entity.ExamSite;
import com.curator.backend.register.exam.site.mapper.ExamSiteMapper;
import com.curator.backend.register.exam.subject.entity.ExamSubject;
import com.curator.backend.register.exam.subject.mapper.ExamSubjectMapper;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 考试报名信息 服务实现类
 *
 * @author Jun
 * @since 2021-05-13
 */
@Service
public class ExamRegisterInfoServiceImpl implements ExamRegisterInfoService {

    @Autowired
    private ExamRegisterInfoMapper examRegisterInfoMapper;
    @Autowired
    private ExamCategoryMapper examCategoryMapper;
    @Autowired
    private ExamSubjectMapper examSubjectMapper;
    @Autowired
    private ExamSiteMapper examSiteMapper;
    @Autowired
    private ExamClassroomMapper examClassroomMapper;
    @DubboReference
    private TestPaperProvider testPaperProvider;

    @Override
    public ResultResponse<PageResult<ExamRegisterInfoDTO>> pageWithExamRegisterInfo(ExamRegisterInfoSearch search) {
        Page<ExamRegisterInfo> page = new Page<>(search.getCurrent(), search.getPageSize());
        QueryWrapper<ExamRegisterInfo> wrapper = new QueryWrapper<>();

        wrapper.like(Help.isNotEmpty(search.getAccountName()), "account_name", search.getAccountName())
                .eq(Help.isNotEmpty(search.getExamCategoryId()), "exam_category_id", search.getExamCategoryId())
                .eq(Help.isNotEmpty(search.getExamSubjectId()), "exam_subject_id", search.getExamSubjectId())
                .eq(Help.isNotEmpty(search.getExamSiteId()), "exam_site_id", search.getExamSiteId())
                .eq(Help.isNotEmpty(search.getPassed()), "is_passed", search.getPassed())
                .orderByDesc("create_time");
        IPage<ExamRegisterInfo> iPage = examRegisterInfoMapper.selectPage(page, wrapper);
        List<ExamRegisterInfoDTO> resultList = iPage.getRecords().stream()
                .map(this::convertEntity).collect(Collectors.toList());

        PageResult<ExamRegisterInfoDTO> resultPage = new PageResult<>();
        BeanUtils.copyProperties(iPage, resultPage, "records");
        resultPage.setRecords(resultList);

        return ResultResponse.<PageResult<ExamRegisterInfoDTO>>builder().success("考试报名信息分页查询成功").data(resultPage).build();
    }

    @Override
    public ResultResponse<?> generateExamPassword(ExamRegisterInfoInfo info) {
        // 给当前考点下的所有考生生成考试口令
        QueryWrapper<ExamRegisterInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("exam_category_id", info.getExamCategoryId())
                .eq("exam_subject_id", info.getExamSubjectId())
                .eq("exam_site_id", info.getExamSiteId());
        List<ExamRegisterInfo> infoList = examRegisterInfoMapper.selectList(wrapper);
        if(Help.isNotEmpty(infoList)) {
            String examPassword = RandomUtil.randomNumbers(6);
            List<String> idList = infoList.parallelStream().map(ExamRegisterInfo::getExamRegisterInfoId).collect(Collectors.toList());
            examRegisterInfoMapper.batchUpdateExamPassword(idList, examPassword, LocalDateTime.now());
        }
        return ResultResponse.builder().success("考试口令生成成功").build();
    }

    @Override
    public ResultResponse<?> assignClassroom(ExamRegisterInfoInfo info) {
        QueryWrapper<ExamRegisterInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("exam_category_id", info.getExamCategoryId())
                .eq("exam_subject_id", info.getExamSubjectId())
                .eq("exam_site_id", info.getExamSiteId());
        List<ExamRegisterInfo> infoList = examRegisterInfoMapper.selectList(wrapper);
        if(Help.isEmpty(infoList)) {
            return ResultResponse.builder().failure("教室分配失败,考生不存在！").build();
        }

        // 首先确定当前科目的报名时间和考试时间
        ExamSubject examSubject = examSubjectMapper.selectById(info.getExamSubjectId());
        if(examSubject.getRegisterStartTime().isAfter(LocalDateTime.now())) {
            return ResultResponse.builder().failure("教室分配失败,当前考试科目报名时间未开始！").build();
        } else if(examSubject.getRegisterEndTime().isAfter(LocalDateTime.now())) {
            return ResultResponse.builder().failure("教室分配失败,当前考试科目报名时间未结束！").build();
        } else if(examSubject.getExamStartTime().isBefore(LocalDateTime.now()) &&
                examSubject.getExamEndTime().isAfter(LocalDateTime.now())) {
            return ResultResponse.builder().failure("教室分配失败,当前考试科目已在进行考试！").build();
        } else if(examSubject.getExamEndTime().isBefore(LocalDateTime.now())) {
            return ResultResponse.builder().failure("教室分配失败,当前考试科目已结束考试！").build();
        }

        int size = infoList.size();
        // 将考生打乱顺序
        for (int i = size - 1; i >= 1; i--) {
            int randomPos = new Random().nextInt(i);
            Collections.swap(infoList, i, randomPos);
        }
        // 查询考点
        ExamSite examSite = examSiteMapper.selectById(info.getExamSiteId());
        // 查询考点下的教室
        QueryWrapper<ExamClassroom> classroomQueryWrapper = new QueryWrapper<>();
        classroomQueryWrapper.eq("exam_site_id", info.getExamSiteId());
        List<ExamClassroom> classroomList = examClassroomMapper.selectList(classroomQueryWrapper);
        if(Help.isEmpty(classroomList)) {
            return ResultResponse.builder().failure("教室分配失败,当前考点没有设置教室！").build();
        }
        // 判断考点下的教室是否能容纳报名的考生
        int limitSum = classroomList.stream().mapToInt(ExamClassroom::getNumberLimit).sum();
        if(size > limitSum) {
            return ResultResponse.builder().failure("教室分配失败,考生数量超过了当前考点下所有考场所能容纳的人数！").build();
        }
        // 为考生分配教室和座位号
        AtomicInteger sort = new AtomicInteger(0);
        StringJoiner joiner = new StringJoiner("");
        // 年份
        String year = String.valueOf(LocalDate.now().getYear()).substring(2);
        // 考点区代码
        String district =  examSite.getDistrict();
        // 考试科目的序列号
        String serialNumWithExamSubject = String.format("%03d",  examSubject.getSerialNum());
        // 考点的序列号
        String serialNumWithExamSite = String.format("%03d",  examSite.getSerialNum());
        joiner.add(year).add(serialNumWithExamSubject).add(district).add(serialNumWithExamSite);
        for (ExamClassroom examClassroom : classroomList) {
            Integer numberLimit = examClassroom.getNumberLimit();
            // 考点下教室的序列号
            String serialNumWithClassroom = String.format("%03d",  examClassroom.getSerialNum());
            joiner.add(serialNumWithClassroom);
            int startNum = sort.get();
            for (int i = startNum; i < size; i++) {
                ExamRegisterInfo registerInfo = infoList.get(i);
                registerInfo.setExamClassroomId(examClassroom.getExamClassroomId());
                registerInfo.setSeatNumber(sort.incrementAndGet());
                joiner.add(String.format("%03d",  registerInfo.getSeatNumber()));
                // 准考证编号
                registerInfo.setAdmissionNumber(joiner.toString());
                if(sort.get() == numberLimit) {
                    // 当前教室的座位分配完了
                    break;
                }
            }
        }
        examRegisterInfoMapper.batchAssignClassroom(infoList, LocalDateTime.now());
        return ResultResponse.builder().success("教室分配成功").build();
    }

    @Override
    public ResultResponse<?> reExam(ExamRegisterInfoInfo info) {
        // 修改考生试卷状态
        testPaperProvider.reExam(info.getExamRegisterInfoId(), info.getPaperStatus());
        // 修改考试报名信息中 相关数据
        examRegisterInfoMapper.updateExamStatusAndScoreAndPassed(info.getExamRegisterInfoId(), LocalDateTime.now());
        return ResultResponse.builder().success("重考设置成功").build();
    }

    @Override
    public List<ExamRegisterInfoExcelDTO> listWithExamRegisterInfoExcel(ExamRegisterInfoSearch search) {
        List<ExamRegisterInfoExcelDTO> resultList = new ArrayList<>();
        QueryWrapper<ExamRegisterInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("exam_category_id", search.getExamCategoryId())
                .eq("exam_subject_id", search.getExamSubjectId())
                .eq("exam_site_id", search.getExamSiteId())
                .eq(Help.isNotEmpty(search.getPassed()), "is_passed", search.getPassed())
                .like(Help.isNotEmpty(search.getAccountName()), "account_name", search.getAccountName())
                .orderByDesc("create_time");
        List<ExamRegisterInfo> infoList = examRegisterInfoMapper.selectList(wrapper);
        if(Help.isNotEmpty(infoList)) {
            ExamCategory examCategory = examCategoryMapper.selectById(search.getExamCategoryId());
            ExamSubject examSubject = examSubjectMapper.selectById(search.getExamSubjectId());
            ExamSite examSite = examSiteMapper.selectById(search.getExamSiteId());
            resultList = infoList.parallelStream().map(info -> {
                ExamRegisterInfoExcelDTO dto = new ExamRegisterInfoExcelDTO();
                dto.setExamCategoryName(examCategory.getExamCategoryName());
                dto.setExamSiteName(examSite.getExamSiteName());
                dto.setExamSubjectName(examSubject.getExamSubjectName());
                dto.setAccountName(info.getAccountName());
                dto.setAdmissionNumber(info.getAdmissionNumber());
                dto.setExamStatusDesc(ExamStatusTypeEnum.getDesc(info.getExamStatus()));
                dto.setScore(String.valueOf(info.getScore()));
                dto.setPassedDesc(1 == info.getPassed() ? "是" : "否");
                return dto;
            }).collect(Collectors.toList());
            return resultList;
        }
        return resultList;
    }



    /**
     * 将 数据库对象 转为 数据传输对象
     *
     * @param entity 数据库信息
     * @return dto实体类
     */
    private ExamRegisterInfoDTO convertEntity(ExamRegisterInfo entity) {
        ExamRegisterInfoDTO target = new ExamRegisterInfoDTO();
        if (Help.isNotEmpty(entity)) {
            BeanUtils.copyProperties(entity, target);
        }
        return target;
    }

}
