package com.curator.backend.register.exam.subject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.backend.register.exam.site.entity.ExamSite;
import com.curator.backend.register.exam.site.mapper.ExamSiteMapper;
import com.curator.backend.register.exam.subject.entity.ExamSubject;
import com.curator.backend.register.exam.subject.entity.ExamSubjectSite;
import com.curator.backend.register.exam.subject.entity.dto.ExamSubjectDTO;
import com.curator.backend.register.exam.subject.entity.dto.ExamSubjectSiteDTO;
import com.curator.backend.register.exam.subject.entity.vo.info.ExamSubjectInfo;
import com.curator.backend.register.exam.subject.entity.vo.info.ExamSubjectSiteInfo;
import com.curator.backend.register.exam.subject.entity.vo.search.ExamSubjectSearch;
import com.curator.backend.register.exam.subject.entity.vo.search.ExamSubjectSiteSearch;
import com.curator.backend.register.exam.subject.mapper.ExamSubjectMapper;
import com.curator.backend.register.exam.subject.mapper.ExamSubjectSiteMapper;
import com.curator.backend.register.exam.subject.service.ExamSubjectService;
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
 * 考试科目 服务实现类
 *
 * @author Jun
 * @since 2021-05-13
 */
@Service
public class ExamSubjectServiceImpl implements ExamSubjectService {

    @Autowired
    private ExamSubjectMapper examSubjectMapper;
    @Autowired
    private ExamSiteMapper examSiteMapper;
    @Autowired
    private ExamSubjectSiteMapper subjectSiteMapper;

    @Override
    public ResultResponse<PageResult<ExamSubjectDTO>> pageWithExamSubject(ExamSubjectSearch search) {
        Page<ExamSubject> page = new Page<>(search.getCurrent(), search.getPageSize());
        QueryWrapper<ExamSubject> wrapper = new QueryWrapper<>();
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(w -> w.eq("parent_account_id", createAccountId)));
        }
        wrapper.like(Help.isNotEmpty(search.getExamSubjectName()), "exam_subject_name", search.getExamSubjectName())
                .eq(Help.isNotEmpty(search.getExamCategoryId()), "exam_category_id", search.getExamCategoryId())
                .ge(Help.isNotEmpty(search.getRegisterStartTime()), "register_start_time", search.getRegisterStartTime())
                .le(Help.isNotEmpty(search.getRegisterEndTime()), "register_end_time", search.getRegisterEndTime())
                .ge(Help.isNotEmpty(search.getExamStartTime()), "exam_start_time", search.getExamStartTime())
                .le(Help.isNotEmpty(search.getExamEndTime()), "exam_end_time", search.getExamEndTime())
                .orderByDesc("create_time");
        IPage<ExamSubject> iPage = examSubjectMapper.selectPage(page, wrapper);
        List<ExamSubjectDTO> resultList = iPage.getRecords().stream()
                .map(this::convertEntity).collect(Collectors.toList());

        PageResult<ExamSubjectDTO> resultPage = new PageResult<>();
        BeanUtils.copyProperties(iPage, resultPage, "records");
        resultPage.setRecords(resultList);

        return ResultResponse.<PageResult<ExamSubjectDTO>>builder().success("考试科目分页查询成功").data(resultPage).build();
    }

    @Override
    public ResultResponse<List<ExamSubjectDTO>> listWithExamSubject(ExamSubjectSearch search) {
        QueryWrapper<ExamSubject> wrapper = new QueryWrapper<>();
        wrapper.like(Help.isNotEmpty(search.getExamSubjectName()), "exam_subject_name", search.getExamSubjectName())
                .eq(Help.isNotEmpty(search.getExamCategoryId()), "exam_category_id", search.getExamCategoryId())
                .orderByDesc("create_time");
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(w -> w.eq("parent_account_id", createAccountId)));
        }
        List<ExamSubject> list = examSubjectMapper.selectList(wrapper);
        List<ExamSubjectDTO> resultList = list.stream().map(this::convertEntity).collect(Collectors.toList());

        return ResultResponse.<List<ExamSubjectDTO>>builder().success("考试科目列表查询成功").data(resultList).build();
    }

    @Override
    public ResultResponse<ExamSubjectDTO> getExamSubject(String id) {
        ExamSubject entity = examSubjectMapper.selectById(id);
        return ResultResponse.<ExamSubjectDTO>builder().success("考试科目查询成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<ExamSubjectDTO> saveExamSubject(ExamSubjectInfo info) {
        ResultResponse<ExamSubjectDTO> res = checkExamSubject(info);
        if (!res.getSucceeded()) {
            return res;
        }
        ExamSubject entity = convertInfo(info);
        entity.setCreateAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID));
        entity.setParentAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_PARENT_ID));
        examSubjectMapper.insert(entity);
        return ResultResponse.<ExamSubjectDTO>builder().success("考试科目添加成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<ExamSubjectDTO> putExamSubject(ExamSubjectInfo info) {
        ResultResponse<ExamSubjectDTO> res = checkExamSubject(info);
        if (!res.getSucceeded()) {
            return res;
        }
        ExamSubject entity = convertInfo(info);
        examSubjectMapper.update(entity, new UpdateWrapper<ExamSubject>().eq("exam_subject_id", info.getExamSubjectId()));
        return ResultResponse.<ExamSubjectDTO>builder().success("考试科目更新成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<String> removeExamSubject(String id) {
        examSubjectMapper.deleteById(id);
        return ResultResponse.<String>builder().success("考试科目删除成功").data(id).build();
    }

    @Override
    public ResultResponse<PageResult<ExamSubjectSiteDTO>> pageWithExamSubjectSite(ExamSubjectSiteSearch search) {
        Page<ExamSubjectSite> page = new Page<>(search.getCurrent(), search.getPageSize());
        QueryWrapper<ExamSubjectSite> wrapper = new QueryWrapper<>();

        wrapper.eq(Help.isNotEmpty(search.getExamSubjectId()), "exam_subject_id", search.getExamSubjectId())
                .eq(Help.isNotEmpty(search.getProvince()), "province", search.getProvince())
                .eq(Help.isNotEmpty(search.getCity()), "city", search.getCity())
                .eq(Help.isNotEmpty(search.getDistrict()), "district", search.getDistrict())
                .orderByDesc("create_time");
        IPage<ExamSubjectSite> iPage = subjectSiteMapper.selectPage(page, wrapper);
        List<ExamSubjectSiteDTO> resultList = iPage.getRecords().stream()
                .map(this::convertEntity).collect(Collectors.toList());

        PageResult<ExamSubjectSiteDTO> resultPage = new PageResult<>();
        BeanUtils.copyProperties(iPage, resultPage, "records");
        resultPage.setRecords(resultList);

        return ResultResponse.<PageResult<ExamSubjectSiteDTO>>builder().success("考试科目下考点分页查询成功").data(resultPage).build();
    }

    @Override
    public ResultResponse<?> addExamSiteToSubject(ExamSubjectSiteInfo info) {
        List<String> examSiteIdList = info.getExamSiteIdList();
        if(Help.isEmpty(examSiteIdList)) {
            return ResultResponse.<String>builder().failure("考点不能为空!").build();
        }
        ExamSubject subject = examSubjectMapper.selectById(info.getExamSubjectId());
        if(Help.isEmpty(subject)) {
            return ResultResponse.<String>builder().failure("考试科目不存在!").build();
        }
        String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
        String parentAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_PARENT_ID);
        examSiteIdList.forEach(examSiteId -> {
            ExamSite examSite = examSiteMapper.selectById(examSiteId);
            if(Help.isNotEmpty(examSite)) {
                ExamSubjectSite entity = new ExamSubjectSite();
                entity.setExamSubjectId(info.getExamSubjectId());
                entity.setExamCategoryId(subject.getExamCategoryId());
                entity.setExamSiteId(examSiteId);
                entity.setProvince(examSite.getProvince());
                entity.setCity(examSite.getCity());
                entity.setDistrict(examSite.getDistrict());
                entity.setCreateAccountId(createAccountId);
                entity.setParentAccountId(parentAccountId);
                subjectSiteMapper.insert(entity);
            }
        });
        return ResultResponse.<String>builder().success("考点成功添加至考试科目中!").build();
    }

    /**
     * 检查考试科目
     *
     * @param info 科目页面信息
     * @return
     */
    private ResultResponse<ExamSubjectDTO> checkExamSubject(ExamSubjectInfo info) {
        if (Help.isEmpty(info.getRegisterStartTime()) || Help.isEmpty(info.getRegisterEndTime())) {
            return ResultResponse.<ExamSubjectDTO>builder().failure("报名开始时间或结束时间不能为空").build();
        } else if (info.getRegisterStartTime().isAfter(info.getRegisterEndTime())) {
            return ResultResponse.<ExamSubjectDTO>builder().failure("报名开始时间不能大于结束时间").build();
        }
        if (Help.isEmpty(info.getExamStartTime()) || Help.isEmpty(info.getExamEndTime())) {
            return ResultResponse.<ExamSubjectDTO>builder().failure("考试开始时间或结束时间不能为空").build();
        } else if (info.getExamStartTime().isAfter(info.getExamEndTime())) {
            return ResultResponse.<ExamSubjectDTO>builder().failure("考试开始时间不能大于结束时间").build();
        }
        if(info.getRegisterEndTime().isAfter(info.getExamStartTime())) {
            return ResultResponse.<ExamSubjectDTO>builder().failure("报名结束时间不能大于考试开始时间").build();
        }
        // 验证科目名称是否已存在
        QueryWrapper<ExamSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("exam_subject_name", info.getExamSubjectName())
                .eq("exam_category_id", info.getExamCategoryId());
        if(Help.isNotEmpty(info.getExamSubjectId())) {
            wrapper.ne("exam_subject_id", info.getExamSubjectId());
        }
        ExamSubject subject = examSubjectMapper.selectOne(wrapper);
        if(Help.isNotEmpty(subject)) {
            return ResultResponse.<ExamSubjectDTO>builder().failure("该考试类型下,科目: " +
                    subject.getExamSubjectName() + "已存在!").build();
        }
        // 验证科目考试时间是否合理(统一考试类型下 各科目的考试时间不能重叠)
        wrapper = new QueryWrapper<>();
        wrapper.eq("exam_category_id", info.getExamCategoryId())
                .and(w -> w.ge( "exam_end_time", info.getExamStartTime())
                        .le( "exam_end_time", info.getExamEndTime())
                        .or(wr ->  wr.le( "exam_start_time", info.getExamStartTime())
                                .ge( "exam_end_time", info.getExamEndTime()))
                        .or(wr ->  wr.ge( "exam_start_time", info.getExamStartTime())
                                .le( "exam_start_time", info.getExamEndTime()))
                );
        if(Help.isNotEmpty(info.getExamSubjectId())) {
            wrapper.ne("exam_subject_id", info.getExamSubjectId());
        }
        subject = examSubjectMapper.selectOne(wrapper);
        if(Help.isNotEmpty(subject)) {
            return ResultResponse.<ExamSubjectDTO>builder().failure("该考试时间段内,科目: " +
                    subject.getExamSubjectName() + "将进行考试,请修改您的考试时间!").build();
        }
        return ResultResponse.<ExamSubjectDTO>builder().success().build();
    }

    /**
     * 将 数据库对象 转为 数据传输对象
     *
     * @param entity 数据库信息
     * @return dto实体类
     */
    private ExamSubjectDTO convertEntity(ExamSubject entity) {
        ExamSubjectDTO target = new ExamSubjectDTO();
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
    private ExamSubject convertInfo(ExamSubjectInfo info) {
        ExamSubject target = new ExamSubject();
        if (Help.isNotEmpty(info)) {
            BeanUtils.copyProperties(info, target);
        }
        return target;
    }

    /**
     * 将 数据库对象 转为 数据传输对象
     *
     * @param entity 数据库信息
     * @return dto实体类
     */
    private ExamSubjectSiteDTO convertEntity(ExamSubjectSite entity) {
        ExamSubjectSiteDTO target = new ExamSubjectSiteDTO();
        if (Help.isNotEmpty(entity)) {
            BeanUtils.copyProperties(entity, target);
            ExamSite examSite = examSiteMapper.selectById(entity.getExamSiteId());
            target.setExamSiteName(examSite.getExamSiteName());
        }
        return target;
    }
}
