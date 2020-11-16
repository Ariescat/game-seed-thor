package com.windforce.common.profile.core;

/**
 * 性能信息统计策略
 *
 * @author Ariescat
 * @version 2020/11/13 13:01
 */
@FunctionalInterface
public interface IProStrategy {

	/**
	 * 处理新值与旧值
	 *
	 * @param oldValue 统计记录中以及存在的值
	 * @param newValue 新产生的值
	 * @return 返回记录值
	 */
	long reAdd(long oldValue, long newValue);
}
