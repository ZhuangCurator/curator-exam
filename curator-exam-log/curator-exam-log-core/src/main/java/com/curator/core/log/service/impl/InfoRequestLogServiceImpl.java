package com.curator.core.log.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.curator.api.log.pojo.dto.InfoRequestLogDTO;
import com.curator.api.log.pojo.vo.info.InfoRequestLogInfo;
import com.curator.api.log.pojo.vo.seacrh.InfoRequestLogSearch;
import com.curator.common.constant.CommonConstant;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;
import com.curator.common.util.Help;
import com.curator.core.log.entity.InfoRequestLog;
import com.curator.core.log.mapper.InfoRequestLogMapper;
import com.curator.core.log.service.InfoRequestLogService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 请求日志信息 服务实现类
 * </p>
 *
 * @author Jun
 * @since 2021-04-17
 */
@Service
public class InfoRequestLogServiceImpl implements InfoRequestLogService {

    @Autowired
    private InfoRequestLogMapper requestLogMapper;

    @Override
    public ResultResponse<PageResult<InfoRequestLogDTO>> pageWithRequestLog(InfoRequestLogSearch search, HttpServletRequest request) {
        Page<InfoRequestLog> page = new Page<>(search.getCurrent(), search.getPageSize());
        QueryWrapper<InfoRequestLog> wrapper = new QueryWrapper<>();
        wrapper.like(Help.isNotEmpty(search.getApplicationName()), "application_name", search.getApplicationName())
                .like(Help.isNotEmpty(search.getControllerName()), "controller_name", search.getControllerName())
                .like(Help.isNotEmpty(search.getMethod()), "method", search.getMethod())
                .eq(Help.isNotEmpty(search.getRequestMethod()), "request_method", search.getRequestMethod())
                .like(Help.isNotEmpty(search.getRequestUrl()), "request_url", search.getRequestUrl())
                .eq(Help.isNotEmpty(search.getStatus()), "status", search.getStatus())
                .orderByDesc("create_time");
        if (Boolean.FALSE.equals(search.getSuperAdmin())) {
            String createAccountId = request.getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID);
            wrapper.and(wr -> wr.eq("create_account_id", createAccountId)
                    .or(w -> w.eq("parent_account_id", createAccountId)));
        }
        IPage<InfoRequestLog> iPage = requestLogMapper.selectPage(page, wrapper);
        List<InfoRequestLogDTO> resultList = iPage.getRecords().stream()
                .map(this::convertEntity).collect(Collectors.toList());

        PageResult<InfoRequestLogDTO> resultPage = new PageResult<>();
        BeanUtils.copyProperties(iPage, resultPage, "records");
        resultPage.setRecords(resultList);

        return ResultResponse.<PageResult<InfoRequestLogDTO>>builder().success("日志信息分页查询成功").data(resultPage).build();
    }

    @Override
    public ResultResponse<InfoRequestLogDTO> saveInfoRequestLog(InfoRequestLogInfo info) {
        InfoRequestLog entity = convertInfo(info);
        requestLogMapper.insert(entity);
        return ResultResponse.<InfoRequestLogDTO>builder().success("日志信息添加成功").data(convertEntity(entity)).build();
    }

    /**
     * 将 数据库对象 转为 数据传输对象
     *
     * @param entity 数据库信息
     * @return dto实体类
     */
    private InfoRequestLogDTO convertEntity(InfoRequestLog entity) {
        InfoRequestLogDTO target = new InfoRequestLogDTO();
        if (Help.isNotEmpty(entity)) {
            BeanUtils.copyProperties(entity, target);
        }
        return target;
    }

    /**
     * 将 页面信息 转为 数据库对象
     *
     * @param info 菜单 页面信息
     * @return 数据库对应实体类
     */
    private InfoRequestLog convertInfo(InfoRequestLogInfo info) {
        InfoRequestLog target = new InfoRequestLog();
        if (Help.isNotEmpty(info)) {
            BeanUtils.copyProperties(info, target);
        }
        return target;
    }
}
