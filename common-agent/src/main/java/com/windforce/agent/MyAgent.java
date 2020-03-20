package com.windforce.agent;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;

/**
 * 代理入口
 *
 * @version 2019/12/20 17:27
 */
public class MyAgent {
	public MyAgent() {
	}

	public static void agentmain(String args, Instrumentation inst) {
		try {
			File file = new File(args);
			byte[] targetClassFile = new byte[(int) file.length()];
			DataInputStream inputStream = new DataInputStream(new FileInputStream(file));
			inputStream.readFully(targetClassFile);
			inputStream.close();

			DynamicClassLoader classLoader = new DynamicClassLoader();
			Class targetClass = classLoader.findClass(targetClassFile);
			Class oldClass = Class.forName(targetClass.getName());

			ClassDefinition classDef = new ClassDefinition(oldClass, targetClassFile);
			inst.redefineClasses(classDef);

			System.err.println("redefineClasses " + args + " success!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
