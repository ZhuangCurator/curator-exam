package com.curator.core.register.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.api.info.provider.InfoCityProvider;
import com.curator.api.register.pojo.dto.ExamSiteDTO;
import com.curator.api.register.pojo.vo.search.ExamSiteSearch;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.core.register.entity.ExamRegisterInfo;
import com.curator.core.register.entity.ExamSite;
import com.curator.core.register.entity.ExamSubjectSite;
import com.curator.core.register.mapper.ExamRegisterInfoMapper;
import com.curator.core.register.mapper.ExamSiteMapper;
import com.curator.core.register.mapper.ExamSubjectSiteMapper;
import com.curator.core.register.service.ExamSiteService;
import org.apache.dubbo.config.annotation.DubboReference;
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
    @Autowired
    private ExamRegisterInfoMapper registerInfoMapper;
    @DubboReference
    private InfoCityProvider cityProvider;

    @Override
    public ResultResponse<PageResult<ExamSiteDTO>> pageWithExamSite(ExamSiteSearch search) {
        QueryWrapper<ExamSubjectSite> wrapper = new QueryWrapper<>();
        wrapper.eq("exam_subject_id", search.getExamSubjectId())
                .eq(Help.isNotEmpty(search.getProvince()), "province", search.getProvince())
                .eq(Help.isNotEmpty(search.getCity()), "city", search.getCity())
                .eq(Help.isNotEmpty(search.getDistrict()), "district", search.getDistrict());
        List<ExamSubjectSite> list = examSubjectSiteMapper.selectList(wrapper);
        if (Help.isEmpty(list)) {
            return ResultResponse.<PageResult<ExamSiteDTO>>builder().failure("当前考试科目下没有设置考点!").build();
        }
        // 过滤出 已报考人数未超出考点限制的考点
        List<ExamSubjectSite> filterList = list.stream().filter(subjectSite -> {
            ExamSite examSite = examSiteMapper.selectById(subjectSite.getExamSiteId());
            Integer count = getRegisterNumberWithSubjectAndSite(search.getExamSubjectId(), examSite.getExamSiteId());
            return examSite.getNumberLimit() > count;
        }).collect(Collectors.toList());
        if (Help.isEmpty(filterList)) {
            return ResultResponse.<PageResult<ExamSiteDTO>>builder().failure("当前考试科目下的考点均已达到人数限制!").build();
        }
        // 查询出考点具体数据
        List<String> examSiteIdList = filterList.stream().map(ExamSubjectSite::getExamSiteId).collect(Collectors.toList());
        Page<ExamSite> page = new Page<>(search.getCurrent(), search.getPageSize());
        QueryWrapper<ExamSite> siteWrapper = new QueryWrapper<>();
        siteWrapper.in("exam_site_id", examSiteIdList)
                .like(Help.isNotEmpty(search.getExamSiteName()), "exam_site_name", search.getExamSiteName());

        IPage<ExamSite> iPage = examSiteMapper.selectPage(page, siteWrapper);

        if (Help.isEmpty(iPage.getRecords())) {
            return ResultResponse.<PageResult<ExamSiteDTO>>builder().failure("当前考试科目下考点均已不存在!").build();
        }
        // 转换考点数据
        List<ExamSiteDTO> resultList = iPage.getRecords().stream().map(this::convertEntity).peek(site -> {
            site.setExamSubjectId(search.getExamSubjectId());
            // 查询已报人数
            Integer count = getRegisterNumberWithSubjectAndSite(search.getExamSubjectId(), site.getExamSiteId());
            site.setRegisterNumber(count);
            if(Help.isNotEmpty(site.getProvince())) {
                site.setProvinceName(cityProvider.getCityName(site.getProvince()).getData());
            }
            if(Help.isNotEmpty(site.getCity())) {
                site.setCityName(cityProvider.getCityName(site.getCity()).getData());
            }
            if(Help.isNotEmpty(site.getDistrict())) {
                site.setDistrictName(cityProvider.getCityName(site.getDistrict()).getData());
            }
        }).collect(Collectors.toList());

        PageResult<ExamSiteDTO> resultPage = new PageResult<>();
        BeanUtils.copyProperties(iPage, resultPage, "records");
        resultPage.setRecords(resultList);

        return ResultResponse.<PageResult<ExamSiteDTO>>builder().success("考点分页数据查询成功!").data(resultPage).build();
    }

    /**
     * 获得科目当前考点下的已报名人数
     *
     * @param examSubjectId 科目ID
     * @param examSiteId 考点ID
     * @return 人数
     */
    private Integer getRegisterNumberWithSubjectAndSite(String examSubjectId, String examSiteId) {
        QueryWrapper<ExamRegisterInfo> infoWrapper = new QueryWrapper<>();
        infoWrapper.eq("exam_subject_id", examSubjectId)
                .eq("exam_site_id", examSiteId);
        Integer count = registerInfoMapper.selectCount(infoWrapper);
        return count != null ? count : 0;
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
