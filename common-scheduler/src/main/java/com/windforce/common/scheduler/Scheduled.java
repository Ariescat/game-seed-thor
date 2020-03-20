package com.windforce.common.scheduler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Kuang Hao
 * @since v1.0 2018年2月11日
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scheduled {

    /**
     * 任务名
     */
    String name();

    /**
     * 表达式值
     */
    String value();

    /**
     * 表达式值类型
     */
    ValueType type() default ValueType.EXPRESSION;

    /**
     * 当无法获取表达式值时使用的默认值
     */
    String defaultValue() default "";

}
