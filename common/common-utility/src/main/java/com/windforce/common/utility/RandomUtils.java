package com.windforce.common.utility;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机数工具类
 *
 * @author frank
 */
public final class RandomUtils {

	/**
	 * 精确到小数点后10位
	 */
	public final static int RATE_BASE = 1000000000;

	/**
	 * 整形概率分母
	 */
	public final static int ODDS_BASE = 10000;

	private RandomUtils() {
		throw new IllegalAccessError("该类不允许实例化");
	}

	/**
	 * 检查是否命中，分母为{@link #ODDS_BASE}
	 *
	 * @param rate 整型命中率，超过分母则必中
	 * @return 命中返回 true, 不命中返回 false
	 */
	public static boolean isHit(double rate) {
		if (rate <= 0) {
			return false;
		}
		if (rate >= ODDS_BASE) {
			return true;
		}
		int value = ThreadLocalRandom.current().nextInt(RATE_BASE);
		return value <= rate;
	}

	/**
	 * 检查是否命中
	 *
	 * @param rate   命中率
	 * @param random 伪随机序列
	 */
	public static boolean isHit(double rate, Random random) {
		if (rate <= 0) {
			return false;
		}
		int limit = (int) (rate * RATE_BASE);
		if (limit >= RATE_BASE) {
			return true;
		}
		int value = random.nextInt(RATE_BASE);
		return value <= limit;
	}

	/**
	 * 获取两个整数之间的随机数
	 *
	 * @param min     最小值
	 * @param max     最大值
	 * @param include 是否包含边界值
	 * @return
	 */
	public static int betweenInt(int min, int max, boolean include) {
		// 参数检查
		if (min > max) {
			throw new IllegalArgumentException("最小值[" + min + "]不能大于最大值[" + max + "]");
		} else if (!include && min == max) {
			throw new IllegalArgumentException("不包括边界值时最小值[" + min + "]不能等于最大值[" + max + "]");
		}
		// 修正边界值
		if (include) {
			max++;
		} else {
			min++;
		}
		return (int) (min + Math.random() * (max - min));
	}
}
