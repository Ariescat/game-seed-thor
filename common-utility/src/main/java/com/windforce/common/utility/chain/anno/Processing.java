package com.windforce.common.utility.chain.anno;

import com.windforce.common.utility.chain.Way;

import java.lang.annotation.*;

/**
 * 处理节点方法声明注解
 *
 * @author frank
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Processing {

	/**
	 * 处理任务名
	 *
	 * @return
	 */
	String name();

	/**
	 * 处理序号
	 *
	 * @return
	 */
	int index() default 0;

	/**
	 * 处理方向
	 *
	 * @return
	 */
	Way way() default Way.IN;
}
