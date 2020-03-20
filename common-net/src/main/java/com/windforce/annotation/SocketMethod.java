package com.windforce.annotation;

import java.lang.annotation.*;

/**
 * 模块声明
 *
 * @author Kuang Hao
 * @since v1.0 2016-1-19
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SocketMethod {
}
