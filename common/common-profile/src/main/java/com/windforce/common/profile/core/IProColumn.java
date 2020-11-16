package com.windforce.common.profile.core;

import org.checkerframework.checker.nullness.compatqual.NonNullType;

/**
 * @author Ariescat
 * @version 2020/11/13 14:26
 */
public interface IProColumn {

	/**
	 * 获取性能信息记录策略
	 *
	 * @return 可以自己实现IProStrategy，也可以使用已定义的{@link ProStrategy}
	 */
	IProStrategy getStrategy();

	/**
	 * 获取列 数值单位 和对应的输出方式
	 */
	default @NonNullType ColumnUnit getColumnUnit() {
		return ColumnUnit.SIMPLE;
	}
}
