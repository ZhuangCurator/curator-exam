package com.curator.common.aspect;

import cn.hutool.core.util.RandomUtil;
import com.curator.common.annotation.Log;
import com.curator.common.constant.CommonConstant;
import com.curator.common.entity.LogResult;
import com.curator.common.enums.LogStatusEnum;
import com.curator.common.util.IpUtil;
import com.curator.common.util.JsonUtil;
import com.curator.common.util.MinioUtil;
import com.curator.common.util.ServletUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

/**
 * 日志切面
 *
 * @author Jun
 * @date @date 2021/4/17
 */
@Aspect
@ConditionalOnClass(RocketMQTemplate.class)
@ConditionalOnProperty(prefix = "rocketmq",name = "logAspect",havingValue = "true")
public class LogAspect {

    @Autowired(required = false)
    RocketMQTemplate rocketMQTemplate;

    private final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    private final Environment environment;

    public LogAspect(Environment environment) {
        this.environment = environment;
    }

    @Pointcut("@annotation(com.curator.common.annotation.Log)")
    public void logPointCut() {
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
        handleLog(joinPoint, null, jsonResult);
    }

    /**
     * 抛出异常时执行
     *
     * @param joinPoint 切点
     * @param e 异常
     */
    @AfterThrowing(value = "logPointCut()",throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Throwable e){
        handleLog(joinPoint, e, null);
    }

    private void handleLog(final JoinPoint joinPoint, final Throwable e, Object jsonResult) {
        try {
            Signature signature = joinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();
            // 获得注解
            Log controllerLog = method.getAnnotation(Log.class);

            // 保存数据库日志
            LogResult logInfo = new LogResult();
            String createAccountName = ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_NAME);
            logInfo.setCreateAccountName(new String(Base64Utils.decodeFromString(createAccountName)));
            logInfo.setCreateAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_ID));
            logInfo.setParentAccountId(ServletUtil.getRequest().getHeader(CommonConstant.HTTP_HEADER_ACCOUNT_PARENT_ID));
            // 应用名
            logInfo.setApplicationName(environment.getProperty("spring.application.name"));
            // 请求状态
            logInfo.setStatus(LogStatusEnum.NORMAL.getStatus());
            // 请求的地址
            String ip = IpUtil.getIpAddr(ServletUtil.getRequest());
            logInfo.setRequestIp(ip);
            // 返回参数
            logInfo.setResultResponse(JsonUtil.obj2String(jsonResult));

            logInfo.setRequestUrl(ServletUtil.getRequest().getRequestURI());

            if (e != null) {
                logInfo.setStatus(LogStatusEnum.EXCEPTION.getStatus());
                String message =ExceptionUtils.getStackTrace(e);
                String fileUrl = MinioUtil.upload("log-bucket", RandomUtil.randomString(10) + ".txt", new ByteArrayInputStream(message.getBytes(StandardCharsets.UTF_8)));
                logInfo.setErrorMsg(fileUrl);
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            logInfo.setMethod(className + "." + methodName + "()");
            // 设置请求方式
            logInfo.setRequestMethod(ServletUtil.getRequest().getMethod());
            // 处理设置注解上的参数
            getControllerMethodParams(joinPoint, controllerLog, logInfo);
            // 保存数据库
            rocketMQTemplate.convertAndSend("logTopic", JsonUtil.obj2String(logInfo));
        } catch (Exception exp) {
            // 记录本地异常日志
            logger.error("==前置通知异常==");
            logger.error("异常信息:{}", exp.getMessage());
        }
    }

    /**
     * 获取参数
     *
     * @param log     日志
     * @param logInfo 操作日志
     * @throws Exception
     */
    public void getControllerMethodParams(JoinPoint joinPoint, Log log, LogResult logInfo) throws Exception {
        // 设置标题
        logInfo.setControllerName(log.controllerName());
        // 设置备注
        logInfo.setRemark(log.remark());
        String params = argsArrayToString(joinPoint.getArgs());
        int strLength = 1024;
        if(params.length() > strLength){
            logInfo.setRequestParam(params.substring(0, strLength));
        }else{
            logInfo.setRequestParam(params);
        }

    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (!isFilterObject(o)) {
                        String jsonObj = JsonUtil.obj2String(o);
                        params.append(jsonObj).append(" ");
                }
            }
        }
        return params.toString().trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    public boolean isFilterObject(final Object o) {
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse;
    }
}
