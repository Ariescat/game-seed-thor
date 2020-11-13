package com.windforce.agent;

/**
 * 动态加载类
 *
 * @version 2019/12/20 17:23
 */
public class DynamicClassLoader extends ClassLoader {
	public DynamicClassLoader() {
	}

	protected Class<?> findClass(byte[] b) throws ClassNotFoundException {
		return defineClass(null, b, 0, b.length);
	}
}
