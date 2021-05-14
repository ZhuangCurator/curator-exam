package com.curator.backend.register.exam.category.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.backend.register.exam.category.entity.ExamCategory;
import com.curator.backend.register.exam.category.entity.dto.ExamCategoryDTO;
import com.curator.backend.register.exam.category.entity.vo.info.ExamCategoryInfo;
import com.curator.backend.register.exam.category.entity.vo.search.ExamCategorySearch;
import com.curator.backend.register.exam.category.mapper.ExamCategoryMapper;
import com.curator.backend.register.exam.category.service.ExamCategoryService;
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
 * 考试类别 服务实现类
 *
 * @author Jun
 * @since 2021-05-13
 */
@Service
public class ExamCategoryServiceImpl implements ExamCategoryService {

    @Autowired
    private ExamCategoryMapper examCategoryMapper;

    @Override
    public ResultResponse<PageResult<ExamCategoryDTO>> pageWithExamCategory(ExamCategorySearch search) {
        Page<ExamCategory> page = new Page<>(search.getCurrent(), search.getPageSize());
        QueryWrapper<ExamCategory> wrapper = new QueryWrapper<>();
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(w -> w.eq("parent_account_id", createAccountId)));
        }
        wrapper.like(Help.isNotEmpty(search.getExamCategoryName()), "exam_category_name", search.getExamCategoryName())
                .ge(Help.isNotEmpty(search.getExamStartTime()), "exam_start_time", search.getExamStartTime())
                .le(Help.isNotEmpty(search.getExamEndTime()), "exam_end_time", search.getExamEndTime())
                .orderByDesc("create_time");
        IPage<ExamCategory> iPage = examCategoryMapper.selectPage(page, wrapper);
        List<ExamCategoryDTO> resultList = iPage.getRecords().stream()
                .map(this::convertEntity).collect(Collectors.toList());

        PageResult<ExamCategoryDTO> resultPage = new PageResult<>();
        BeanUtils.copyProperties(iPage, resultPage, "records");
        resultPage.setRecords(resultList);

        return ResultResponse.<PageResult<ExamCategoryDTO>>builder().success("考试类别分页查询成功").data(resultPage).build();
    }

    @Override
    public ResultResponse<List<ExamCategoryDTO>> listWithExamCategory(ExamCategorySearch search) {
        QueryWrapper<ExamCategory> wrapper = new QueryWrapper<>();
        wrapper.like(Help.isNotEmpty(search.getExamCategoryName()), "exam_category_name", search.getExamCategoryName())
                .orderByDesc("create_time");
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(w -> w.eq("parent_account_id", createAccountId)));
        }
        List<ExamCategory> list = examCategoryMapper.selectList(wrapper);
        List<ExamCategoryDTO> resultList = list.stream().map(this::convertEntity).collect(Collectors.toList());

        return ResultResponse.<List<ExamCategoryDTO>>builder().success("考试类别列表查询成功").data(resultList).build();
    }

    @Override
    public ResultResponse<ExamCategoryDTO> getExamCategory(String id) {
        ExamCategory entity = examCategoryMapper.selectById(id);
        return ResultResponse.<ExamCategoryDTO>builder().success("考试类别查询成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<ExamCategoryDTO> saveExamCategory(ExamCategoryInfo info) {
        ResultResponse<ExamCategoryDTO> res = checkExamCategory(info);
        if (!res.getSucceeded()) {
            return res;
        }
        ExamCategory entity = convertInfo(info);
        entity.setCreateAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID));
        entity.setParentAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_PARENT_ID));
        examCategoryMapper.insert(entity);
        return ResultResponse.<ExamCategoryDTO>builder().success("考试类别添加成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<ExamCategoryDTO> putExamCategory(ExamCategoryInfo info) {
        ResultResponse<ExamCategoryDTO> res = checkExamCategory(info);
        if (!res.getSucceeded()) {
            return res;
        }
        ExamCategory entity = convertInfo(info);
        examCategoryMapper.update(entity, new UpdateWrapper<ExamCategory>().eq("exam_category_id", info.getExamCategoryId()));
        return ResultResponse.<ExamCategoryDTO>builder().success("考试类别更新成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<String> removeExamCategory(String id) {
        examCategoryMapper.deleteById(id);
        return ResultResponse.<String>builder().success("考试类别删除成功").data(id).build();
    }

    /**
     * 检查考试类别
     *
     * @param info 类别页面信息
     * @return
     */
    private ResultResponse<ExamCategoryDTO> checkExamCategory(ExamCategoryInfo info) {
        if (Help.isEmpty(info.getExamStartTime()) || Help.isEmpty(info.getExamEndTime())) {
            return ResultResponse.<ExamCategoryDTO>builder().failure("考试开始时间或结束时间不能为空").build();
        } else if (info.getExamStartTime().isAfter(info.getExamEndTime())) {
            return ResultResponse.<ExamCategoryDTO>builder().failure("考试开始时间不能大于结束时间").build();
        }
        return ResultResponse.<ExamCategoryDTO>builder().success().build();
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

    /**
     * 将 页面信息 转为 数据库对象
     *
     * @param info 页面信息
     * @return 数据库对应实体类
     */
    private ExamCategory convertInfo(ExamCategoryInfo info) {
        ExamCategory target = new ExamCategory();
        if (Help.isNotEmpty(info)) {
            BeanUtils.copyProperties(info, target);
        }
        return target;
    }
}
