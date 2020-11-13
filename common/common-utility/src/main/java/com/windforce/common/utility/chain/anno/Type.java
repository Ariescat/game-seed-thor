package com.windforce.common.utility.chain.anno;

/**
 * 注入类型
 *
 * @author frank
 */
public enum Type {

	/**
	 * 整个通知对象
	 */
	NOTICE,
	/**
	 * 内容主体(默认值)
	 */
	CONTENT,
	/**
	 * 步骤
	 */
	STEP,
	/**
	 * 进入/返回
	 */
	WAY;

}
