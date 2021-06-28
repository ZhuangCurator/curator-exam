package com.curator.core.log.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 请求日志信息
 * </p>
 *
 * @author Jun
 * @since 2021-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InfoRequestLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请求日志主键
     */
    @TableId(value = "request_log_id", type = IdType.ASSIGN_ID)
    private String requestLogId;

    /**
     * 服务名称
     */
    private String applicationName;

    /**
     * 控制器名称
     */
    private String controllerName;

    /**
     * 请求方法名
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
     * 请求ip地址
     */
    private String requestIp;

    /**
     * 请求耗时
     */
    private Integer timeConsume;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 返回结果
     */
    private String resultResponse;

    /**
     * 1-正常 2-异常
     */
    private Integer status;

    /**
     * 异常消息
     */
    private String errorMsg;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建账户名称
     */
    private String createAccountName;

    /**
     * 创建账户id
     */
    private String createAccountId;

    /**
     * 创建账户父账户id
     */
    private String parentAccountId;

    /**
     * 1 表示删除，0 表示未删除
     */
    @TableLogic
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    private Integer deleted;

    /**
     * 插入时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
