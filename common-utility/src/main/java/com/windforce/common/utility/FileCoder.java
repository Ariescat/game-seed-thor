package com.windforce.common.utility;

import org.apache.commons.io.FileUtils;

import java.io.File;

public class FileCoder {

	public static File encodeFile(File srcFile, String key) {
		try {
			byte[] fileBytes = FileUtils.readFileToByteArray(srcFile);
			String prefix = srcFile.getName().substring(0, srcFile.getName().indexOf("."));
			String suffix = srcFile.getName().substring(srcFile.getName().indexOf("."));
			File targetFile = File.createTempFile(prefix, suffix);
			byte[] result = CryptUtils.encrypt(fileBytes, key.getBytes());
			FileUtils.writeByteArrayToFile(targetFile, result);
			return targetFile;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static File decodeFile(File srcFile, String key) {
		try {
			byte[] fileBytes = FileUtils.readFileToByteArray(srcFile);
			String prefix = srcFile.getName().substring(0, srcFile.getName().indexOf("."));
			String suffix = srcFile.getName().substring(srcFile.getName().indexOf("."));
			File targetFile = File.createTempFile(prefix, suffix);
			byte[] result = CryptUtils.decrypt(fileBytes, key.getBytes());
			FileUtils.writeByteArrayToFile(targetFile, result);
			return targetFile;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public static byte[] decodeFileToBytes(File srcFile, String key) {
		try {
			byte[] fileBytes = FileUtils.readFileToByteArray(srcFile);
			byte[] result = CryptUtils.decrypt(fileBytes, key.getBytes());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
