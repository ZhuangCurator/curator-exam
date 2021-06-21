package com.curator.backend.register.exam.site.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.api.info.pojo.dto.AccountDTO;
import com.curator.api.info.provider.InfoAccountProvider;
import com.curator.api.info.provider.InfoCityProvider;
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
import com.curator.common.util.JsonUtil;
import com.curator.common.util.ServletUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.dubbo.config.annotation.DubboReference;
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
    @DubboReference
    private InfoCityProvider cityProvider;
    @DubboReference
    private InfoAccountProvider infoAccountProvider;

    @Override
    public ResultResponse<PageResult<ExamSiteDTO>> pageWithExamSite(ExamSiteSearch search) {
        Page<ExamSite> page = new Page<>(search.getCurrent(), search.getPageSize());
        QueryWrapper<ExamSite> wrapper = new QueryWrapper<>();
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            String childrenAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_CHILDREN_ID);
            List<String> childrenAccountIdList = JsonUtil.string2Obj(childrenAccountId, new TypeReference<List<String>>(){});
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(Help.isNotEmpty(childrenAccountIdList), w -> w.in("create_account_id", childrenAccountIdList)));
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
            String childrenAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_CHILDREN_ID);
            String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            List<String> childrenAccountIdList = JsonUtil.string2Obj(childrenAccountId, new TypeReference<List<String>>(){});
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(Help.isNotEmpty(childrenAccountIdList), w -> w.in("create_account_id", childrenAccountIdList)));
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
        // 保存地市信息
        saveInfoCity(info);
        ExamSite entity = convertInfo(info);
        // 查询当前地区下考点的最大序列号
        Integer serialNum = examSiteMapper.selectMaxSerialNum(entity.getDistrict());
        if(serialNum != null) {
            entity.setSerialNum(serialNum + 1);
        } else {
            entity.setSerialNum(1);
        }
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
        // 保存地市信息
        saveInfoCity(info);
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
            if(Help.isNotEmpty(entity.getProvince())) {
                target.setProvinceName(cityProvider.getCityName(entity.getProvince()).getData());
            }
            if(Help.isNotEmpty(entity.getCity())) {
                target.setCityName(cityProvider.getCityName(entity.getCity()).getData());
            }
            if(Help.isNotEmpty(entity.getDistrict())) {
                target.setDistrictName(cityProvider.getCityName(entity.getDistrict()).getData());
            }
            AccountDTO accountDTO = infoAccountProvider.getAccount(entity.getCreateAccountId()).getData();
            if(Help.isNotEmpty(accountDTO)) {
                target.setCreateAccountName(accountDTO.getAccountName());
            }
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

    /**
     * 保存地市信息
     *
     * @param info
     */
    private void saveInfoCity(ExamSiteInfo info) {
        cityProvider.saveInfoCity(info.getProvince(), info.getProvinceName());
        cityProvider.saveInfoCity(info.getCity(), info.getCityName());
        cityProvider.saveInfoCity(info.getDistrict(), info.getDistrictName());
    }
}
