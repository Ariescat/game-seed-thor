package com.windforce.common.utility;

/**
 * 文件工具
 *
 * @author Ariescat
 * @version 2020/11/13 15:08
 */
public class FileHelper {

	/**
	 * 格式化文件大小
	 */
	public static String formatFileSize(long size) {
		StringBuilder builder = new StringBuilder();

		if (size >= FileSize.GB_COEFFICIENT) {
			long gb = size / FileSize.GB_COEFFICIENT;
			size -= gb * FileSize.GB_COEFFICIENT;
			builder.append(gb).append("g ");
		}
		if (size >= FileSize.MB_COEFFICIENT) {
			long mb = size / FileSize.MB_COEFFICIENT;
			size -= mb * FileSize.MB_COEFFICIENT;
			builder.append(mb).append("m ");
		}
		if (size >= FileSize.KB_COEFFICIENT) {
			long kb = size / FileSize.KB_COEFFICIENT;
			size -= kb * FileSize.KB_COEFFICIENT;
			builder.append(kb).append("kb ");
		}
		if (size > 0) {
			builder.append(size).append("b");
		}
		return builder.toString().trim();
	}
}
