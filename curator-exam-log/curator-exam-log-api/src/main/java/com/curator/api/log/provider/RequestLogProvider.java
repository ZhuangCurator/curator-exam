package com.curator.api.log.provider;

import com.curator.api.log.pojo.dto.InfoRequestLogDTO;
import com.curator.api.log.pojo.vo.seacrh.InfoRequestLogSearch;
import com.curator.common.support.PageResult;
import com.curator.common.support.ResultResponse;

/**
 * 请求日志RPC接口
 *
 * @author Jun
 * @date 2021/4/17
 */
public interface RequestLogProvider {

    /**
     * 请求日志信息 分页查询
     *
     * @param search {@link InfoRequestLogSearch}
     * @return 分页结果
     */
    ResultResponse<PageResult<InfoRequestLogDTO>> pageWithRequestLog(InfoRequestLogSearch search);
 }
