package com.curator.core.register.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.curator.api.register.pojo.dto.ExamSiteDTO;
import com.curator.api.register.pojo.vo.search.ExamSiteSearch;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.core.register.entity.ExamSite;
import com.curator.core.register.entity.ExamSubjectSite;
import com.curator.core.register.mapper.ExamSiteMapper;
import com.curator.core.register.mapper.ExamSubjectSiteMapper;
import com.curator.core.register.service.ExamSiteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jun
 * @date 2021/5/17
 */
@Service
public class ExamSiteServiceImpl implements ExamSiteService {

    @Autowired
    private ExamSubjectSiteMapper examSubjectSiteMapper;
    @Autowired
    private ExamSiteMapper examSiteMapper;

    @Override
    public ResultResponse<List<ExamSiteDTO>> listWithExamSite(ExamSiteSearch search) {
        QueryWrapper<ExamSubjectSite> wrapper = new QueryWrapper<>();
        wrapper.eq("exam_category_id", search.getExamCategoryId())
                .eq("exam_subject_id", search.getExamSubjectId());
        List<ExamSubjectSite> list = examSubjectSiteMapper.selectList(wrapper);
        if (Help.isEmpty(list)) {
            return ResultResponse.<List<ExamSiteDTO>>builder().failure("当前考试科目下没有设置考点!").build();
        }
        // 过滤出 已报考人数未超出考点限制的考点
        List<ExamSubjectSite> filterList = list.stream().filter(subjectSite -> {
            ExamSite examSite = examSiteMapper.selectById(subjectSite.getExamSiteId());
            return examSite.getNumberLimit() > subjectSite.getRegisterNumber();
        }).collect(Collectors.toList());
        if (Help.isEmpty(filterList)) {
            return ResultResponse.<List<ExamSiteDTO>>builder().failure("当前考试科目下的考点均已达到人数限制!").build();
        }
        // 查询出考点具体数据
        List<String> examSiteIdList = filterList.stream().map(ExamSubjectSite::getExamSiteId).collect(Collectors.toList());
        QueryWrapper<ExamSite> siteWrapper = new QueryWrapper<>();
        siteWrapper.in("exam_site_id", examSiteIdList);
        List<ExamSite> examSiteList = examSiteMapper.selectList(siteWrapper);
        if (Help.isEmpty(examSiteList)) {
            return ResultResponse.<List<ExamSiteDTO>>builder().failure("当前考试科目下考点均已不存在!").build();
        }
        // 转换考点数据
        List<ExamSiteDTO> resultList = examSiteList.stream().map(this::convertEntity).peek(site -> {
            site.setExamCategoryId(search.getExamCategoryId());
            site.setExamSubjectId(search.getExamSubjectId());
            // 查询已报人数
            QueryWrapper<ExamSubjectSite> subjectSiteWrapper = new QueryWrapper<>();
            subjectSiteWrapper.eq("exam_category_id", search.getExamCategoryId())
                            .eq("exam_subject_id", search.getExamSubjectId())
                            .eq("exam_site_id", site.getExamSiteId());
            ExamSubjectSite subjectSite  = examSubjectSiteMapper.selectOne(subjectSiteWrapper);
            site.setRegisterNumber(subjectSite.getRegisterNumber());
        }).collect(Collectors.toList());
        return ResultResponse.<List<ExamSiteDTO>>builder().success("考点列表查询成功!").data(resultList).build();
    }

    /**
     * 将 数据库对象 转为 数据传输对象
     *
     * @param entity 数据库信息
     * @return dto实体类
     */
    private ExamSiteDTO convertEntity(ExamSite entity) {
        ExamSiteDTO target = new ExamSiteDTO();
        if (Help.isNotEmpty(entity)) {
            BeanUtils.copyProperties(entity, target);
        }
        return target;
    }
}
