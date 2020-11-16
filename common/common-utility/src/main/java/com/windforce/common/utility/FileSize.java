package com.windforce.common.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copy From ch.qos.logback.core.util.FileSize
 *
 * @author Ariescat
 * @version 2020/11/13 15:01
 */
public class FileSize {

	private final static String LENGTH_PART = "([0-9]+)";
	private final static int DOUBLE_GROUP = 1;

	private final static String UNIT_PART = "(|kb|mb|gb)s?";
	private final static int UNIT_GROUP = 2;

	private static final Pattern FILE_SIZE_PATTERN = Pattern.compile(LENGTH_PART + "\\s*" + UNIT_PART, Pattern.CASE_INSENSITIVE);

	static public final long KB_COEFFICIENT = 1024;
	static public final long MB_COEFFICIENT = 1024 * KB_COEFFICIENT;
	static public final long GB_COEFFICIENT = 1024 * MB_COEFFICIENT;

	final long size;

	public FileSize(long size) {
		this.size = size;
	}

	public long getSize() {
		return size;
	}

	static public FileSize valueOf(String fileSizeStr) {
		Matcher matcher = FILE_SIZE_PATTERN.matcher(fileSizeStr);

		long coefficient;
		if (matcher.matches()) {
			String lenStr = matcher.group(DOUBLE_GROUP);
			String unitStr = matcher.group(UNIT_GROUP);

			long lenValue = Long.parseLong(lenStr);
			if (unitStr.equalsIgnoreCase("")) {
				coefficient = 1;
			} else if (unitStr.equalsIgnoreCase("kb")) {
				coefficient = KB_COEFFICIENT;
			} else if (unitStr.equalsIgnoreCase("mb")) {
				coefficient = MB_COEFFICIENT;
			} else if (unitStr.equalsIgnoreCase("gb")) {
				coefficient = GB_COEFFICIENT;
			} else {
				throw new IllegalStateException("Unexpected " + unitStr);
			}
			return new FileSize(lenValue * coefficient);
		} else {
			throw new IllegalArgumentException("String value [" + fileSizeStr + "] is not in the expected format.");
		}
	}

	@Override
	public String toString() {
		return FileHelper.formatFileSize(size);
	}
}
