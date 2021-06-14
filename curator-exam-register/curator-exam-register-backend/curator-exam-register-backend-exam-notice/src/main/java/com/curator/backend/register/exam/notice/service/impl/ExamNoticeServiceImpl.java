package com.curator.backend.register.exam.notice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.backend.register.exam.notice.entity.ExamNotice;
import com.curator.backend.register.exam.notice.entity.dto.ExamNoticeDTO;
import com.curator.backend.register.exam.notice.entity.vo.info.ExamNoticeInfo;
import com.curator.backend.register.exam.notice.entity.vo.search.ExamNoticeSearch;
import com.curator.backend.register.exam.notice.mapper.ExamNoticeMapper;
import com.curator.backend.register.exam.notice.service.ExamNoticeService;
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
 * 考试公告 服务实现类
 *
 * @author Jun
 * @since 2021-05-13
 */
@Service
public class ExamNoticeServiceImpl implements ExamNoticeService {

    @Autowired
    private ExamNoticeMapper examNoticeMapper;

    @Override
    public ResultResponse<PageResult<ExamNoticeDTO>> pageWithExamNotice(ExamNoticeSearch search) {
        Page<ExamNotice> page = new Page<>(search.getCurrent(), search.getPageSize());
        QueryWrapper<ExamNotice> wrapper = new QueryWrapper<>();
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            generateWrapper(wrapper);
        }
        wrapper.like(Help.isNotEmpty(search.getExamNoticeName()), "exam_notice_name", search.getExamNoticeName())
                .orderByDesc("create_time");
        IPage<ExamNotice> iPage = examNoticeMapper.selectPage(page, wrapper);
        List<ExamNoticeDTO> resultList = iPage.getRecords().stream()
                .map(this::convertEntity).collect(Collectors.toList());

        PageResult<ExamNoticeDTO> resultPage = new PageResult<>();
        BeanUtils.copyProperties(iPage, resultPage, "records");
        resultPage.setRecords(resultList);

        return ResultResponse.<PageResult<ExamNoticeDTO>>builder().success("考试公告分页查询成功").data(resultPage).build();
    }

    @Override
    public ResultResponse<List<ExamNoticeDTO>> listWithExamNotice(ExamNoticeSearch search) {
        QueryWrapper<ExamNotice> wrapper = new QueryWrapper<>();
        wrapper.like(Help.isNotEmpty(search.getExamNoticeName()), "exam_notice_name", search.getExamNoticeName())
                .orderByDesc("create_time");
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            generateWrapper(wrapper);
        }
        List<ExamNotice> list = examNoticeMapper.selectList(wrapper);
        List<ExamNoticeDTO> resultList = list.stream().map(this::convertEntity).collect(Collectors.toList());

        return ResultResponse.<List<ExamNoticeDTO>>builder().success("考试公告列表查询成功").data(resultList).build();
    }

    @Override
    public ResultResponse<ExamNoticeDTO> getExamNotice(String id) {
        ExamNotice entity = examNoticeMapper.selectById(id);
        return ResultResponse.<ExamNoticeDTO>builder().success("考试公告查询成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<ExamNoticeDTO> saveExamNotice(ExamNoticeInfo info) {
        ExamNotice entity = convertInfo(info);
        entity.setProvince(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_PROVINCE));
        entity.setCity(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_CITY));
        entity.setCreateAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID));
        entity.setParentAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_PARENT_ID));
        examNoticeMapper.insert(entity);
        return ResultResponse.<ExamNoticeDTO>builder().success("考试公告添加成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<ExamNoticeDTO> putExamNotice(ExamNoticeInfo info) {
        ExamNotice entity = convertInfo(info);
        examNoticeMapper.update(entity, new UpdateWrapper<ExamNotice>().eq("exam_notice_id", info.getExamNoticeId()));
        return ResultResponse.<ExamNoticeDTO>builder().success("考试公告更新成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<String> removeExamNotice(String id) {
        examNoticeMapper.deleteById(id);
        return ResultResponse.<String>builder().success("考试公告删除成功").data(id).build();
    }

    /**
     * 构建查询wrapper
     *
     * @param wrapper
     */
    private void generateWrapper(QueryWrapper<ExamNotice> wrapper) {
        String province = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_PROVINCE);
        String city = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_CITY);
        // 省级管理员能看到全省的和超级管理员创建的，市级管理员能看到全市的和省级管理员以及超级管理员创建的
        String defaultCode = "0";
        wrapper.and(wr -> wr.eq("province", defaultCode).eq("city", defaultCode)
                .or(w -> w.eq("province", province).eq("city", defaultCode))
                .or(city.equals(defaultCode), w -> w.eq("province", province))
                .or(!city.equals(defaultCode), w -> w.eq("province", province).eq("city", city)));
    }

    /**
     * 将 数据库对象 转为 数据传输对象
     *
     * @param entity 数据库信息
     * @return dto实体类
     */
    private ExamNoticeDTO convertEntity(ExamNotice entity) {
        ExamNoticeDTO target = new ExamNoticeDTO();
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
    private ExamNotice convertInfo(ExamNoticeInfo info) {
        ExamNotice target = new ExamNotice();
        if (Help.isNotEmpty(info)) {
            BeanUtils.copyProperties(info, target);
        }
        return target;
    }
}
