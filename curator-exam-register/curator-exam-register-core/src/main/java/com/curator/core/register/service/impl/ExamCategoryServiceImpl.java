package com.curator.core.register.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.api.register.pojo.dto.ExamCategoryDTO;
import com.curator.api.register.pojo.vo.search.ExamCategorySearch;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.core.register.entity.ExamCategory;
import com.curator.core.register.mapper.ExamCategoryMapper;
import com.curator.core.register.service.ExamCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 考试类别 服务实现类
 *
 * @author Jun
 * @date 2021/5/17
 */
@Service
public class ExamCategoryServiceImpl implements ExamCategoryService {

    @Autowired
    private ExamCategoryMapper examCategoryMapper;

    @Override
    public ResultResponse<PageResult<ExamCategoryDTO>> pageWithExamCategory(ExamCategorySearch search) {
        if (Help.isEmpty(search.getProvince()) || Help.isEmpty(search.getCity())) {
            return ResultResponse.<PageResult<ExamCategoryDTO>>builder().failure("请先选择地市!").build();
        }
        Page<ExamCategory> page = new Page<>(search.getCurrent(), search.getPageSize());
        // 根据地市查询考试类别
        QueryWrapper<ExamCategory> wrapper = new QueryWrapper<>();
        String defaultCode = "0";
        wrapper.like(Help.isNotEmpty(search.getExamCategoryName()), "exam_category_name", search.getExamCategoryName())
                .and(wr -> wr.eq("province", defaultCode).eq("city", defaultCode)
                .or(w -> w.eq("province", search.getProvince()).eq("city", defaultCode))
                .or(w -> w.eq("province", search.getProvince()).eq("city", search.getCity())))
                .orderByDesc("create_time");
        IPage<ExamCategory> iPage = examCategoryMapper.selectPage(page, wrapper);
        if (Help.isEmpty(iPage.getRecords())) {
            return ResultResponse.<PageResult<ExamCategoryDTO>>builder().failure("当前地市下不存在可报名的考试类别!").build();
        }
        List<ExamCategory> filterList = iPage.getRecords().stream().filter(category ->
                category.getExamEndTime().isAfter(LocalDateTime.now())).collect(Collectors.toList());
        if (Help.isEmpty(filterList)) {
            // 考试结束时间都过了
            return ResultResponse.<PageResult<ExamCategoryDTO>>builder().failure("当前地市下所有的考试类别都已考试结束!").build();
        }
        List<ExamCategoryDTO> resultList = filterList.stream().map(this::convertEntity).collect(Collectors.toList());

        PageResult<ExamCategoryDTO> resultPage = new PageResult<>();
        BeanUtils.copyProperties(iPage, resultPage, "records");
        resultPage.setRecords(resultList);
        return ResultResponse.<PageResult<ExamCategoryDTO>>builder().success("考试类别分页查询成功!").data(resultPage).build();
    }

    /**
     * 将 数据库对象 转为 数据传输对象
     *
     * @param entity 数据库信息
     * @return dto实体类
     */
    private ExamCategoryDTO convertEntity(ExamCategory entity) {
        ExamCategoryDTO target = new ExamCategoryDTO();
        if (Help.isNotEmpty(entity)) {
            BeanUtils.copyProperties(entity, target);
        }
        return target;
    }
}
