package com.windforce.common.profile.core;

/**
 * 性能统计常用测量
 *
 * @author Ariescat
 * @version 2020/11/13 12:33
 */
public enum ProStrategy implements IProStrategy {

	/**
	 * 求和
	 */
	sum {
		@Override
		public long reAdd(long oldValue, long newValue) {
			return oldValue + newValue;
		}
	},
	/**
	 * 最大值
	 */
	max {
		@Override
		public long reAdd(long oldValue, long newValue) {
			return Math.max(oldValue, newValue);
		}
	},
	/**
	 * 最小值
	 */
	min {
		@Override
		public long reAdd(long oldValue, long newValue) {
			return Math.min(oldValue, newValue);
		}
	};

	/**
	 * 平均值
	 */
	public static IProStrategy avg() {
		long[] countAndSum = new long[2];
		return (oldValue, newValue) -> {
			countAndSum[0]++;
			countAndSum[1] += newValue;
			// 这会带来一定程度的精度丢失
			return countAndSum[1] / countAndSum[0];
		};
	}

}
