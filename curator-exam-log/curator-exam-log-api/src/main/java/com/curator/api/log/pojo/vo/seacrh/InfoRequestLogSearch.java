package com.curator.api.log.pojo.vo.seacrh;

import com.curator.common.support.BaseSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 账户分页查询条件
 *
 * @author Jun
 * @date 2021/4/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InfoRequestLogSearch extends BaseSearch {

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 控制器名称
     */
    private String controllerName;

    /**
     * 方法名
     */
    private String method;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 请求url
     */
    private String requestUrl;

    /**
     * 请求状态， 1-正常 2-异常
     */
    private String status;

}
