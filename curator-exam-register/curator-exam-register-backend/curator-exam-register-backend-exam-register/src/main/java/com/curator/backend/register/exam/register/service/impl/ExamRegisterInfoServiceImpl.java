package com.curator.backend.register.exam.register.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.backend.register.exam.register.entity.ExamRegisterInfo;
import com.curator.backend.register.exam.register.entity.dto.ExamRegisterInfoDTO;
import com.curator.backend.register.exam.register.entity.vo.search.ExamRegisterInfoSearch;
import com.curator.backend.register.exam.register.mapper.ExamRegisterInfoMapper;
import com.curator.backend.register.exam.register.service.ExamRegisterInfoService;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public ResultResponse<PageResult<ExamRegisterInfoDTO>> pageWithExamRegisterInfo(ExamRegisterInfoSearch search) {
        Page<ExamRegisterInfo> page = new Page<>(search.getCurrent(), search.getPageSize());
        QueryWrapper<ExamRegisterInfo> wrapper = new QueryWrapper<>();

        wrapper.like(Help.isNotEmpty(search.getAccountName()), "account_name", search.getAccountName())
                .orderByDesc("create_time");
        IPage<ExamRegisterInfo> iPage = examRegisterInfoMapper.selectPage(page, wrapper);
        List<ExamRegisterInfoDTO> resultList = iPage.getRecords().stream()
                .map(this::convertEntity).collect(Collectors.toList());

        PageResult<ExamRegisterInfoDTO> resultPage = new PageResult<>();
        BeanUtils.copyProperties(iPage, resultPage, "records");
        resultPage.setRecords(resultList);

        return ResultResponse.<PageResult<ExamRegisterInfoDTO>>builder().success("考试报名信息分页查询成功").data(resultPage).build();
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
