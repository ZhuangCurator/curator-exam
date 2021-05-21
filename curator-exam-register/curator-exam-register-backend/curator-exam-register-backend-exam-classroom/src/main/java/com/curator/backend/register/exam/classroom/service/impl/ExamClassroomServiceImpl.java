package com.curator.backend.register.exam.classroom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.backend.register.exam.classroom.entity.ExamClassroom;
import com.curator.backend.register.exam.classroom.entity.dto.ExamClassroomDTO;
import com.curator.backend.register.exam.classroom.entity.vo.info.ExamClassroomInfo;
import com.curator.backend.register.exam.classroom.entity.vo.search.ExamClassroomSearch;
import com.curator.backend.register.exam.classroom.mapper.ExamClassroomMapper;
import com.curator.backend.register.exam.classroom.service.ExamClassroomService;
import com.curator.backend.register.exam.site.entity.ExamSite;
import com.curator.backend.register.exam.site.entity.dto.ExamSiteDTO;
import com.curator.backend.register.exam.site.entity.vo.info.ExamSiteInfo;
import com.curator.backend.register.exam.site.mapper.ExamSiteMapper;
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
 * 教室 服务实现类
 *
 * @author Jun
 * @since 2021-05-13
 */
@Service
public class ExamClassroomServiceImpl implements ExamClassroomService {

    @Autowired
    private ExamClassroomMapper examClassroomMapper;
    @Autowired
    private ExamSiteMapper examSiteMapper;

    @Override
    public ResultResponse<PageResult<ExamClassroomDTO>> pageWithExamClassroom(ExamClassroomSearch search) {
        Page<ExamClassroom> page = new Page<>(search.getCurrent(), search.getPageSize());
        QueryWrapper<ExamClassroom> wrapper = new QueryWrapper<>();
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(w -> w.eq("parent_account_id", createAccountId)));
        }
        wrapper.like(Help.isNotEmpty(search.getExamClassroomName()), "exam_classroom_name", search.getExamClassroomName())
                .eq(Help.isNotEmpty(search.getExamSiteId()), "exam_site_id", search.getExamSiteId())
                .orderByDesc("create_time");
        IPage<ExamClassroom> iPage = examClassroomMapper.selectPage(page, wrapper);
        List<ExamClassroomDTO> resultList = iPage.getRecords().stream()
                .map(this::convertEntity).collect(Collectors.toList());

        PageResult<ExamClassroomDTO> resultPage = new PageResult<>();
        BeanUtils.copyProperties(iPage, resultPage, "records");
        resultPage.setRecords(resultList);

        return ResultResponse.<PageResult<ExamClassroomDTO>>builder().success("教室分页查询成功").data(resultPage).build();
    }

    @Override
    public ResultResponse<List<ExamClassroomDTO>> listWithExamClassroom(ExamClassroomSearch search) {
        QueryWrapper<ExamClassroom> wrapper = new QueryWrapper<>();
        wrapper.like(Help.isNotEmpty(search.getExamClassroomName()), "exam_classroom_name", search.getExamClassroomName())
                .eq(Help.isNotEmpty(search.getExamSiteId()), "exam_site_id", search.getExamSiteId())
                .orderByDesc("create_time");
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(w -> w.eq("parent_account_id", createAccountId)));
        }
        List<ExamClassroom> list = examClassroomMapper.selectList(wrapper);
        List<ExamClassroomDTO> resultList = list.stream().map(this::convertEntity).collect(Collectors.toList());

        return ResultResponse.<List<ExamClassroomDTO>>builder().success("教室列表查询成功").data(resultList).build();
    }

    @Override
    public ResultResponse<ExamClassroomDTO> getExamClassroom(String id) {
        ExamClassroom entity = examClassroomMapper.selectById(id);
        return ResultResponse.<ExamClassroomDTO>builder().success("教室查询成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<ExamClassroomDTO> saveExamClassroom(ExamClassroomInfo info) {
        ResultResponse<ExamClassroomDTO> res = checkExamClassroom(info);
        if(!res.getSucceeded()) {
            return res;
        }
        ExamClassroom entity = convertInfo(info);
        // 查询当前考试类目下科目的最大序列号
        Integer serialNum = examClassroomMapper.selectMaxSerialNum(entity.getExamSiteId());
        if(serialNum != null) {
            entity.setSerialNum(serialNum + 1);
        }
        entity.setCreateAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID));
        entity.setParentAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_PARENT_ID));
        examClassroomMapper.insert(entity);
        return ResultResponse.<ExamClassroomDTO>builder().success("教室添加成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<ExamClassroomDTO> putExamClassroom(ExamClassroomInfo info) {
        ResultResponse<ExamClassroomDTO> res = checkExamClassroom(info);
        if(!res.getSucceeded()) {
            return res;
        }
        ExamClassroom entity = convertInfo(info);
        examClassroomMapper.update(entity, new UpdateWrapper<ExamClassroom>().eq("exam_classroom_id", info.getExamClassroomId()));
        return ResultResponse.<ExamClassroomDTO>builder().success("教室更新成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<String> removeExamClassroom(String id) {
        examClassroomMapper.deleteById(id);
        return ResultResponse.<String>builder().success("教室删除成功").data(id).build();
    }

    /**
     * 检查教室
     *
     * @param info 教室页面信息
     * @return
     */
    private ResultResponse<ExamClassroomDTO> checkExamClassroom(ExamClassroomInfo info) {
        // 验证教室名称是否已存在
        QueryWrapper<ExamClassroom> wrapper = new QueryWrapper<>();
        wrapper.eq("exam_classroom_name", info.getExamClassroomName())
                .eq("exam_site_id", info.getExamSiteId());
        if(Help.isNotEmpty(info.getExamClassroomId())) {
            wrapper.ne("exam_classroom_id", info.getExamClassroomId());
        }
        ExamClassroom entity = examClassroomMapper.selectOne(wrapper);
        if(Help.isNotEmpty(entity)) {
            return ResultResponse.<ExamClassroomDTO>builder().failure("该考点下，教室: " +
                    entity.getExamClassroomName() + "已存在!").build();
        }
        // 检查各教室的人数限制加起来是否超过了考点
        ExamSite examSite = examSiteMapper.selectById(info.getExamSiteId());
        wrapper = new QueryWrapper<>();
        wrapper.eq("exam_site_id", info.getExamSiteId());
        if(Help.isNotEmpty(info.getExamClassroomId())) {
            wrapper.ne("exam_classroom_id", info.getExamClassroomId());
        }
        List<ExamClassroom> classroomList = examClassroomMapper.selectList(wrapper);
        if(Help.isNotEmpty(classroomList)) {
            int numberLimit = classroomList.stream().mapToInt(ExamClassroom::getNumberLimit).sum();
            numberLimit = numberLimit + info.getNumberLimit();
            if(numberLimit > examSite.getNumberLimit()) {
                return ResultResponse.<ExamClassroomDTO>builder().failure("该考点下，各教室的人数限制加起来超过了考点人数限制！").build();
            }
        }
        return ResultResponse.<ExamClassroomDTO>builder().success().build();
    }

    /**
     * 将 数据库对象 转为 数据传输对象
     *
     * @param entity 数据库信息
     * @return dto实体类
     */
    private ExamClassroomDTO convertEntity(ExamClassroom entity) {
        ExamClassroomDTO target = new ExamClassroomDTO();
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
    private ExamClassroom convertInfo(ExamClassroomInfo info) {
        ExamClassroom target = new ExamClassroom();
        if (Help.isNotEmpty(info)) {
            BeanUtils.copyProperties(info, target);
        }
        return target;
    }
}
