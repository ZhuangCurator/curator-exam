package com.curator.backend.info.power.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.api.info.enums.InfoPowerStatusEnum;
import com.curator.backend.info.power.entity.InfoPower;
import com.curator.backend.info.power.entity.dto.InfoPowerDTO;
import com.curator.backend.info.power.entity.vo.info.InfoPowerInfo;
import com.curator.backend.info.power.entity.vo.search.InfoPowerSearch;
import com.curator.backend.info.power.mapper.InfoPowerMapper;
import com.curator.backend.info.power.service.InfoPowerService;
import com.curator.common.constant.CommonConstant;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.common.util.ServletUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限信息 服务实现类
 *
 * @author Jun
 * @date 2021/4/17
 */
@Service
public class InfoPowerServiceImpl implements InfoPowerService {

    @Autowired
    private InfoPowerMapper powerMapper;

    @Override
    public ResultResponse<PageResult<InfoPowerDTO>> pageWithInfoPower(InfoPowerSearch search) {
        Page<InfoPower> page = new Page<>(search.getCurrent(), search.getPageSize());
        QueryWrapper<InfoPower> wrapper = new QueryWrapper<>();
        wrapper.like(Help.isNotEmpty(search.getPowerName()), "power_name", search.getPowerName())
                .eq(Help.isNotEmpty(search.getPowerType()), "power_type", search.getPowerType())
                .eq(Help.isNotEmpty(search.getPowerStatus()), "power_status", search.getPowerStatus())
                .orderByDesc("create_time");
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(w -> w.eq("parent_account_id", createAccountId)));
        }
        IPage<InfoPower> iPage = powerMapper.selectPage(page, wrapper);
        List<InfoPowerDTO> resultList = iPage.getRecords().stream()
                .map(this::convertEntity).collect(Collectors.toList());

        PageResult<InfoPowerDTO> resultPage = new PageResult<>();
        BeanUtils.copyProperties(iPage, resultPage, "records");
        resultPage.setRecords(resultList);

        return ResultResponse.<PageResult<InfoPowerDTO>>builder().success("权限分页查询成功").data(resultPage).build();
    }

    @Override
    public ResultResponse<List<InfoPowerDTO>> listWithInfoPower(InfoPowerSearch search) {
        QueryWrapper<InfoPower> wrapper = new QueryWrapper<>();
        wrapper.like(Help.isNotEmpty(search.getPowerName()), "power_name", search.getPowerName())
                .eq("power_status", InfoPowerStatusEnum.ENABLE.getStatus())
                .orderByDesc("create_time");
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(w -> w.eq("parent_account_id", createAccountId)));
        }
        List<InfoPower> list = powerMapper.selectList(wrapper);
        List<InfoPowerDTO> resultList = list.stream().map(this::convertEntity).collect(Collectors.toList());

        return ResultResponse.<List<InfoPowerDTO>>builder().success("权限列表查询成功").data(resultList).build();
    }

    @Override
    public ResultResponse<InfoPowerDTO> getInfoPower(String infoPowerId) {
        InfoPower entity = powerMapper.selectById(infoPowerId);
        return ResultResponse.<InfoPowerDTO>builder().success("权限查询成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<InfoPowerDTO> saveInfoPower(InfoPowerInfo info) {
        InfoPower entity = convertInfo(info);
        // 默认启用
        entity.setPowerStatus(InfoPowerStatusEnum.ENABLE.getStatus());
        entity.setCreateAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID));
        entity.setParentAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_PARENT_ID));
        powerMapper.insert(entity);
        return ResultResponse.<InfoPowerDTO>builder().success("权限添加成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<InfoPowerDTO> putInfoPower(InfoPowerInfo info) {
        InfoPower entity = convertInfo(info);
        powerMapper.update(entity, new UpdateWrapper<InfoPower>().eq("power_id", info.getPowerId()));
        return ResultResponse.<InfoPowerDTO>builder().success("权限更新成功").data(convertEntity(entity)).build();
    }

    @Override
    public ResultResponse<String> removeInfoPower(String infoPowerId) {
        powerMapper.deleteById(infoPowerId);
        return ResultResponse.<String>builder().success("权限删除成功").data(infoPowerId).build();
    }

    /**
     * 将 数据库对象 转为 数据传输对象
     *
     * @param entity 数据库信息
     * @return dto实体类
     */
    private InfoPowerDTO convertEntity(InfoPower entity) {
        InfoPowerDTO target = new InfoPowerDTO();
        if (Help.isNotEmpty(entity)) {
            BeanUtils.copyProperties(entity, target);
        }
        return target;
    }

    /**
     * 将 页面信息 转为 数据库对象
     *
     * @param info 权限 页面信息
     * @return 数据库对应实体类
     */
    private InfoPower convertInfo(InfoPowerInfo info) {
        InfoPower target = new InfoPower();
        if (Help.isNotEmpty(info)) {
            BeanUtils.copyProperties(info, target);
        }
        return target;
    }
}
