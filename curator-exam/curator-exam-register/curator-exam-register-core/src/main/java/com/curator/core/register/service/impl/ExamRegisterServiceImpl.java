package com.curator.core.register.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.api.info.pojo.dto.AccountDTO;
import com.curator.api.info.provider.InfoAccountProvider;
import com.curator.api.info.provider.InfoCityProvider;
import com.curator.api.register.enums.ExamStatusTypeEnum;
import com.curator.api.register.pojo.dto.ExamNoticeDTO;
import com.curator.api.register.pojo.dto.ExamRegisterInfoDTO;
import com.curator.api.register.pojo.vo.info.ExamRegisterInfoInfo;
import com.curator.api.register.pojo.vo.search.ExamRegisterInfoSearch;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.common.util.RedissonUtil;
import com.curator.core.register.entity.*;
import com.curator.core.register.mapper.*;
import com.curator.core.register.service.ExamRegisterService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 考生报名信息 服务实现类
 *
 * @author Jun
 * @date 2021/5/17
 */
@Service
public class ExamRegisterServiceImpl implements ExamRegisterService {

    @Autowired
    private ExamRegisterInfoMapper registerInfoMapper;
    @Autowired
    private ExamCategoryMapper examCategoryMapper;
    @Autowired
    private ExamSubjectMapper examSubjectMapper;
    @Autowired
    private ExamSiteMapper examSiteMapper;
    @Autowired
    private ExamClassroomMapper examClassroomMapper;
    @DubboReference
    private InfoAccountProvider accountProvider;

    @Override
    public ResultResponse<String> accountRegister(ExamRegisterInfoInfo info) {
        // 判断考试类别/考试科目/考点是否已被删除
        ExamCategory examCategory = examCategoryMapper.selectById(info.getExamCategoryId());
        if (Help.isEmpty(examCategory)) {
            return ResultResponse.<String>builder().failure("该考试类别已不存在!").build();
        }
        ExamSubject examSubject = examSubjectMapper.selectById(info.getExamSubjectId());
        if (Help.isEmpty(examSubject)) {
            return ResultResponse.<String>builder().failure("该考试科目已不存在!").build();
        }
        ExamSite examSite = examSiteMapper.selectById(info.getExamSiteId());
        if (Help.isEmpty(examSite)) {
            return ResultResponse.<String>builder().failure("该考点已不存在!").build();
        }
        // 判断是否已经报名此科目
        QueryWrapper<ExamRegisterInfo> infoWrapper = new QueryWrapper<>();
        infoWrapper.eq("exam_subject_id", info.getExamSubjectId())
                .eq("account_id", info.getAccountId());
        ExamRegisterInfo registerInfo = registerInfoMapper.selectOne(infoWrapper);
        if (Help.isNotEmpty(registerInfo)) {
            return ResultResponse.<String>builder().failure(examSubject.getExamSubjectName() + " 此科目该考生已报名!").build();
        }
        // 考生进行报名
        String lockName = "REGISTER:ACCOUNT";
        RedissonUtil.lock(lockName, 5000);
        try {
            // 检查当前科目考点的报名人数是否超出限制
            infoWrapper = new QueryWrapper<>();
            infoWrapper.eq("exam_subject_id", info.getExamSubjectId())
                    .eq("exam_site_id", info.getExamSiteId());
            Integer count = registerInfoMapper.selectCount(infoWrapper);
            if (count != null && count > examSite.getNumberLimit()) {
                return ResultResponse.<String>builder().failure("当前考点报名人数已满,请选择其他的考点进行报名!").build();
            }
            // 判断是否已经报名此科目
            QueryWrapper<ExamRegisterInfo> registerInfoWrapper = new QueryWrapper<>();
            registerInfoWrapper.eq("account_id", info.getAccountId())
                    .eq("exam_subject_id", info.getExamSubjectId());
            ExamRegisterInfo examRegisterInfo = registerInfoMapper.selectOne(registerInfoWrapper);
            if (Help.isNotEmpty(examRegisterInfo)) {
                return ResultResponse.<String>builder().failure(examSubject.getExamSubjectName() + " 此科目该考生已报名!").build();
            }
            // 添加报名记录
            ExamRegisterInfo entity = new ExamRegisterInfo();
            entity.setExamCategoryId(info.getExamCategoryId());
            entity.setExamSubjectId(info.getExamSubjectId());
            entity.setExamSiteId(info.getExamSiteId());
            entity.setAccountId(info.getAccountId());
            entity.setAccountName(info.getAccountName());
            ResultResponse<AccountDTO> res = accountProvider.getAccount(entity.getAccountId());
            entity.setIdCard(res.getData().getIdCard());
            entity.setExamStatus(ExamStatusTypeEnum.UN_STARTED.getStatus());
            entity.setPassed(0);
            registerInfoMapper.insert(entity);
            return ResultResponse.<String>builder().success("考生报名成功!").data(entity.getExamRegisterInfoId()).build();
        } finally {
            RedissonUtil.unlock(lockName);
        }
    }

    @Override
    public ResultResponse<PageResult<ExamRegisterInfoDTO>> pageWithRegisterInfo(ExamRegisterInfoSearch search) {
        Page<ExamRegisterInfo> page = new Page<>(search.getCurrent(), search.getPageSize());
        QueryWrapper<ExamRegisterInfo> wrapper = new QueryWrapper<>();

        wrapper.eq("account_id", search.getAccountId())
                .orderByDesc("create_time");

        IPage<ExamRegisterInfo> iPage = registerInfoMapper.selectPage(page, wrapper);
        List<ExamRegisterInfoDTO> resultList = iPage.getRecords().stream()
                .map(this::convertEntity).collect(Collectors.toList());

        PageResult<ExamRegisterInfoDTO> resultPage = new PageResult<>();
        BeanUtils.copyProperties(iPage, resultPage, "records");
        resultPage.setRecords(resultList);

        return ResultResponse.<PageResult<ExamRegisterInfoDTO>>builder().success("考试信息页查询成功").data(resultPage).build();
    }

    @Override
    public ResultResponse<ExamRegisterInfoDTO> getRegisterInfo(String examRegisterInfoId) {
        ExamRegisterInfo info = registerInfoMapper.selectById(examRegisterInfoId);
        return ResultResponse.<ExamRegisterInfoDTO>builder().success("考试信息查询成功").data(convertEntity(info)).build();
    }

    @Override
    public ResultResponse<?> previewAdmissionTicket(String examRegisterInfoId) {
        ExamRegisterInfo info = registerInfoMapper.selectById(examRegisterInfoId);
        if(Help.isEmpty(info.getAdmissionNumber())) {
            return ResultResponse.builder().failure("准考证编号未生成，请继续等待！").build();
        }
        return ResultResponse.builder().success("准考证编号已生成！").build();
    }

    /**
     * 将 数据库对象 转为 数据传输对象
     *
     * @param entity 数据库信息
     * @return dto实体类
     */
    private ExamRegisterInfoDTO convertEntity(ExamRegisterInfo entity) {
        ExamRegisterInfoDTO target = new ExamRegisterInfoDTO();
        if (Help.isNotEmpty(entity)) {
            BeanUtils.copyProperties(entity, target);
            if (Help.isNotEmpty(entity.getExamCategoryId())) {
                ExamCategory examCategory = examCategoryMapper.selectById(entity.getExamCategoryId());
                if (Help.isNotEmpty(examCategory)) {
                    target.setExamCategoryName(examCategory.getExamCategoryName());
                }
            }
            if (Help.isNotEmpty(entity.getExamSubjectId())) {
                ExamSubject examSubject = examSubjectMapper.selectById(entity.getExamSubjectId());
                if (Help.isNotEmpty(examSubject)) {
                    target.setExamSubjectName(examSubject.getExamSubjectName());
                    target.setExamStartTime(examSubject.getExamStartTime());
                    target.setExamEndTime(examSubject.getExamEndTime());
                }
            }
            ExamSite examSite = examSiteMapper.selectById(entity.getExamSiteId());
            target.setExamSiteName(examSite.getExamSiteName());
            target.setExamSiteAddress(examSite.getAddress());

            if(Help.isNotEmpty(entity.getExamClassroomId())) {
                ExamClassroom classroom = examClassroomMapper.selectById(entity.getExamClassroomId());
                if(Help.isNotEmpty(classroom)) {
                    target.setExamClassroomName(classroom.getExamClassroomName());
                    target.setExamClassroomAddress(classroom.getAddress());
                }
            }
        }
        return target;
    }
}
