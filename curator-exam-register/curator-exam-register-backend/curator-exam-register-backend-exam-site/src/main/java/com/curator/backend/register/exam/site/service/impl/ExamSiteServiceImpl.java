package com.curator.backend.register.exam.site.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.backend.register.exam.site.entity.ExamSite;
import com.curator.backend.register.exam.site.entity.dto.ExamSiteDTO;
import com.curator.backend.register.exam.site.entity.vo.info.ExamSiteInfo;
import com.curator.backend.register.exam.site.entity.vo.search.ExamSiteSearch;
import com.curator.backend.register.exam.site.mapper.ExamSiteMapper;
import com.curator.backend.register.exam.site.service.ExamSiteService;
import com.curator.common.constant.CommonConstant;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.common.util.ServletUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 考点 服务实现类
 *
 * @author Jun
 * @since 2021-05-13
 */
@Service
public class ExamSiteServiceImpl implements ExamSiteService {

    @Autowired
    private ExamSiteMapper examSiteMapper;

    @Override
    public ResultResponse<PageResult<ExamSiteDTO>> pageWithExamSite(ExamSiteSearch search) {
        Page<ExamSite> page = new Page<>(search.getCurrent(), search.getPageSize());
        QueryWrapper<ExamSite> wrapper = new QueryWrapper<>();
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(w -> w.eq("parent_account_id", createAccountId)));
        }
        wrapper.like(Help.isNotEmpty(search.getExamSiteName()), "exam_site_name", search.getExamSiteName())
                .eq(Help.isNotEmpty(search.getProvince()), "province", search.getProvince())
                .eq(Help.isNotEmpty(search.getCity()), "city", search.getCity())
                .eq(Help.isNotEmpty(search.getDistrict()), "district", search.getDistrict())
                .orderByDesc("create_time");
        IPage<ExamSite> iPage = examSiteMapper.selectPage(page, wrapper);
        List<ExamSiteDTO> resultList = iPage.getRecords().stream()
                .map(this::convertEntity).collect(Collectors.toList());

        PageResult<ExamSiteDTO> resultPage = new PageResult<>();
        BeanUtils.copyProperties(iPage, resultPage, "records");
        resultPage.setRecords(resultList);

        return ResultResponse.<PageResult<ExamSiteDTO>>builder().success("考点分页查询成功").data(resultPage).build();
    }

    @Override
    public ResultResponse<List<ExamSiteDTO>> listWithExamSite(ExamSiteSearch search) {
        QueryWrapper<ExamSite> wrapper = new QueryWrapper<>();
        wrapper.like(Help.isNotEmpty(search.getExamSiteName()), "exam_site_name", search.getExamSiteName())
                .eq(Help.isNotEmpty(search.getProvince()), "province", search.getProvince())
                .eq(Help.isNotEmpty(search.getCity()), "city", search.getCity())
                .eq(Help.isNotEmpty(search.getDistrict()), "district", search.getDistrict())
                .orderByDesc("create_time");
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(w -> w.eq("parent_account_id", createAccountId)));
        }
        List<ExamSite> list = examSiteMapper.selectList(wrapper);
        List<ExamSiteDTO> resultList = list.stream().map(this::convertEntity).collect(Collectors.toList());

        return ResultResponse.<List<ExamSiteDTO>>builder().success("考点列表查询成功").data(resultList).build();
    }

    @Override
    public ResultResponse<ExamSiteDTO> getExamSite(String id) {
        ExamSite entity = examSiteMapper.selectById(id);
        return ResultResponse.<ExamSiteDTO>builder().success("考点查询成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<ExamSiteDTO> saveExamSite(ExamSiteInfo info) {
        ResultResponse<ExamSiteDTO> res = checkExamSite(info);
        if(!res.getSucceeded()) {
            return res;
        }
        ExamSite entity = convertInfo(info);
        entity.setCreateAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID));
        entity.setParentAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_PARENT_ID));
        examSiteMapper.insert(entity);
        return ResultResponse.<ExamSiteDTO>builder().success("考点添加成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<ExamSiteDTO> putExamSite(ExamSiteInfo info) {
        ResultResponse<ExamSiteDTO> res = checkExamSite(info);
        if(!res.getSucceeded()) {
            return res;
        }
        ExamSite entity = convertInfo(info);
        examSiteMapper.update(entity, new UpdateWrapper<ExamSite>().eq("exam_site_id", info.getExamSiteId()));
        return ResultResponse.<ExamSiteDTO>builder().success("考点更新成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<String> removeExamSite(String id) {
        examSiteMapper.deleteById(id);
        return ResultResponse.<String>builder().success("考点删除成功").data(id).build();
    }

    /**
     * 检查考点
     *
     * @param info 考点页面信息
     * @return
     */
    private ResultResponse<ExamSiteDTO> checkExamSite(ExamSiteInfo info) {
        // 验证考点名称是否已存在
        QueryWrapper<ExamSite> wrapper = new QueryWrapper<>();
        wrapper.eq("exam_site_name", info.getExamSiteName());
        if(Help.isNotEmpty(info.getExamSiteId())) {
            wrapper.ne("exam_site_id", info.getExamSiteId());
        }
        ExamSite entity = examSiteMapper.selectOne(wrapper);
        if(Help.isNotEmpty(entity)) {
            return ResultResponse.<ExamSiteDTO>builder().failure("考点: " +
                    entity.getExamSiteName() + "已存在!").build();
        }
        return ResultResponse.<ExamSiteDTO>builder().success().build();
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

    /**
     * 将 页面信息 转为 数据库对象
     *
     * @param info 页面信息
     * @return 数据库对应实体类
     */
    private ExamSite convertInfo(ExamSiteInfo info) {
        ExamSite target = new ExamSite();
        if (Help.isNotEmpty(info)) {
            BeanUtils.copyProperties(info, target);
        }
        return target;
    }
}
