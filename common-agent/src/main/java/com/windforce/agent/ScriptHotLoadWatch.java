package com.windforce.agent;

import com.sun.tools.attach.VirtualMachine;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Date;

/**
 * 热更
 *
 * @version 2019/12/20 17:38
 */
public class ScriptHotLoadWatch {

	private static final String AGENT_JAR_NAME = "common-agent-1.1.jar";
	private String agentJarPath;

	public static void start(String libPath, String scriptPath) throws Exception {
		File jarFile = new File(libPath + File.separator + AGENT_JAR_NAME);
		if (!jarFile.exists()) {
			System.err.println("未找到热更包，热更未启动");
			return;
		}

		ScriptHotLoadWatch scriptHotLoadWatch = new ScriptHotLoadWatch();
		scriptHotLoadWatch.agentJarPath = jarFile.getAbsolutePath();
		scriptHotLoadWatch.startWatch(scriptPath);
	}

	/**
	 * 启动热更脚本目录监听
	 *
	 * @param scriptPath 监听目录
	 * @throws Exception 如果脚本目录不存在，抛出异常
	 */
	private void startWatch(String scriptPath) throws Exception {
		File file = new File(scriptPath);
		if (!file.exists()) {
			throw new RuntimeException("没有找到文件夹！" + scriptPath);
		}

		FileAlterationObserver observer = new FileAlterationObserver(file);
		observer.addListener(new FileAlterationListenerAdaptor() {
			@Override
			public void onFileDelete(File file) {
				System.err.println(new Date() + ": onFileDelete:" + file.getName());
			}

			@Override
			public void onFileCreate(File file) {
				System.err.println(new Date() + ": onFileCreate" + file.getName());
				if (!file.getName().endsWith(".class")) {
					return;
				}
				updateClass(file);
			}

			@Override
			public void onFileChange(File file) {
				System.err.println(new Date() + ": onFileChange" + file.getName());
				if (!file.getName().endsWith(".class")) {
					return;
				}
				updateClass(file);
			}
		});

		FileAlterationMonitor monitor = new FileAlterationMonitor(3000);
		monitor.addObserver(observer);
		monitor.start();

		System.out.println("start watch script " + file.getAbsolutePath());
	}

	private void updateClass(File file) {
		try {
			String path = file.getAbsolutePath();
			String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
			VirtualMachine vm = VirtualMachine.attach(pid);
			vm.loadAgent(agentJarPath, path);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
