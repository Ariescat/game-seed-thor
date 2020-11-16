package com.windforce.common.profile.core;


import com.windforce.common.utility.FileHelper;

/**
 * 列记录数据单位
 *
 * @author Ariescat
 * @version 2020/11/13 14:35
 */
public enum ColumnUnit {

	/**
	 * 无
	 */
	SIMPLE(""),

	/**
	 * 纳秒转毫秒
	 */
	NANO_TO_MS("ms") {
		@Override
		public long transferValue(long input) {
			return input / 1000;
		}

		@Override
		public String convertValue(long input) {
			return String.format("%.2f", input / 100F);
		}
	},
	/**
	 * 文件大小
	 */
	FILE_SIZE("byte") {
		@Override
		public String convertValue(long input) {
			return FileHelper.formatFileSize(input);
		}
	};

	/**
	 * 列 单位的名称
	 */
	private final String unitName;

	ColumnUnit(String unitName) {
		this.unitName = unitName;
	}

	/**
	 * 数值转换
	 */
	public long transferValue(long input) {
		return input;
	}

	/**
	 * 数值输出格式化转换
	 */
	public String convertValue(long input) {
		return String.valueOf(input);
	}

	public String getUnitName() {
		return unitName;
	}
}
