package com.curator.core.register.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.api.register.pojo.dto.ExamNoticeDTO;
import com.curator.api.register.pojo.vo.search.ExamNoticeSearch;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.core.register.entity.ExamNotice;
import com.curator.core.register.mapper.ExamNoticeMapper;
import com.curator.core.register.service.ExamNoticeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 考试公告 服务实现类
 *
 * @author Jun
 * @date 2021/6/14
 */
@Service
public class ExamNoticeServiceImpl implements ExamNoticeService {

    @Autowired
    private ExamNoticeMapper examNoticeMapper;

    @Override
    public ResultResponse<PageResult<ExamNoticeDTO>> pageWithExamNotice(ExamNoticeSearch search) {
        Page<ExamNotice> page = new Page<>(search.getCurrent(), search.getPageSize());
        QueryWrapper<ExamNotice> wrapper = new QueryWrapper<>();

        wrapper.like(Help.isNotEmpty(search.getExamNoticeName()), "exam_notice_name", search.getExamNoticeName());
        String defaultCode = "0";
        wrapper.and(wr -> wr.eq("province", defaultCode).eq("city", defaultCode)
                .or(Help.isNotEmpty(search.getProvince()), w -> w.eq("province", search.getProvince()).eq("city", defaultCode))
                .or(Help.isNotEmpty(search.getProvince()) && Help.isNotEmpty(search.getCity()),
                        w -> w.eq("province", search.getProvince()).eq("city", search.getCity())))
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
    public ResultResponse<ExamNoticeDTO> getExamNotice(String id) {
        ExamNotice entity = examNoticeMapper.selectById(id);
        return ResultResponse.<ExamNoticeDTO>builder().success("考试公告查询成功").data(convertEntity(entity)).build();
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
}
