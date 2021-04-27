package com.curator.core.log.service;

import com.curator.api.log.pojo.dto.InfoRequestLogDTO;
import com.curator.api.log.pojo.vo.info.InfoRequestLogInfo;
import com.curator.api.log.pojo.vo.seacrh.InfoRequestLogSearch;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 请求日志信息 服务类
 * </p>
 *
 * @author Jun
 * @since 2021-04-17
 */
public interface InfoRequestLogService {

    /**
     * 请求日志信息 分页查询
     *
     * @param search {@link InfoRequestLogSearch}
     * @return 分页结果
     */
    ResultResponse<PageResult<InfoRequestLogDTO>> pageWithRequestLog(InfoRequestLogSearch search, HttpServletRequest request);

    /**
     * 添加日志信息
     *
     * @param info 日志信息
     * @return {@link ResultResponse}
     */
    ResultResponse<InfoRequestLogDTO> saveInfoRequestLog(InfoRequestLogInfo info);
}
