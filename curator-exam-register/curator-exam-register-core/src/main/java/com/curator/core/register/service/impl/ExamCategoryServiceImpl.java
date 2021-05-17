package com.curator.core.register.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.curator.api.register.pojo.dto.ExamCategoryDTO;
import com.curator.api.register.pojo.vo.search.ExamCategorySearch;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.core.register.entity.ExamCategory;
import com.curator.core.register.entity.ExamSubjectSite;
import com.curator.core.register.mapper.ExamCategoryMapper;
import com.curator.core.register.mapper.ExamSubjectSiteMapper;
import com.curator.core.register.service.ExamCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
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
    private ExamSubjectSiteMapper examSubjectSiteMapper;
    @Autowired
    private ExamCategoryMapper examCategoryMapper;
    
    @Override
    public ResultResponse<List<ExamCategoryDTO>> listWithExamCategory(ExamCategorySearch search) {
        // 根据地市查询考试类别
        QueryWrapper<ExamSubjectSite> wrapper = new QueryWrapper<>();
        wrapper.eq(Help.isNotEmpty(search.getProvince()), "province", search.getProvince())
                .eq(Help.isNotEmpty(search.getCity()), "city", search.getCity())
                .eq(Help.isNotEmpty(search.getDistrict()), "district", search.getDistrict());
        List<ExamSubjectSite> list = examSubjectSiteMapper.selectList(wrapper);
        Set<String> examCategoryIdSet = list.stream().map(ExamSubjectSite::getExamCategoryId).collect(Collectors.toSet());
        if(Help.isEmpty(examCategoryIdSet)) {
            return ResultResponse.<List<ExamCategoryDTO>>builder().failure("当前地市下没有设置考试类别!").build();
        }
        // 查询考试类别
        QueryWrapper<ExamCategory> categoryWrapper = new QueryWrapper<>();
        categoryWrapper.in("exam_category_id", examCategoryIdSet)
                        .ge("exam_end_time", LocalDateTime.now())
                        .orderByDesc("create_time");
        List<ExamCategory> categoryList = examCategoryMapper.selectList(categoryWrapper);
        if(Help.isEmpty(categoryList)) {
            // 类别或许被删了 或许考试结束时间小于当前时间
            return ResultResponse.<List<ExamCategoryDTO>>builder().failure("当前地市下不存在可报名的考试类别!").build();
        }
        List<ExamCategoryDTO> resultList = categoryList.stream().map(this::convertEntity).collect(Collectors.toList());
        return ResultResponse.<List<ExamCategoryDTO>>builder().success("考试类别查询成功!").data(resultList).build();
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
