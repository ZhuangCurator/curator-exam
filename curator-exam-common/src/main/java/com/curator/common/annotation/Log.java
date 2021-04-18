package com.curator.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义操作日志记录注解
 *
 * @author Jun
 * @date 2021/4/17
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

    /**
     * 控制器名称
     */
    String controllerName() default "";

    /**
     * 备注
     */
    String remark() default "";

}
