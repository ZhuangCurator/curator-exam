package com.curator.api.info.provider;

import com.curator.common.support.ResultResponse;

/**
 * 地市信息 RPC接口
 *
 * @author Jun
 * @date 2021/6/6
 */
public interface InfoCityProvider {

    /**
     * 获取地市名称
     *
     * @param code 地市行政代码
     * @return 地市名称
     */
    ResultResponse<String> getCityName(String code);

    /**
     * 保存地市名称及行政代码
     *
     * @param code 地市行政代码
     * @param name 地市名称
     * @return 地市行政代码
     */
    ResultResponse<String> saveInfoCity(String code, String name);
}
