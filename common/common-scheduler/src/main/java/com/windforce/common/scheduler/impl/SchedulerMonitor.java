package com.windforce.common.scheduler.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;


@Service
public class SchedulerMonitor implements SchedulerMonitorMBean {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SimpleScheduler scheduler;

	@PostConstruct
	protected void init() {
		// 注册MBean
		try {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			ObjectName name = new ObjectName("com.my9yu.common:type=SchedulerMBean");
			mbs.registerMBean(this, name);
		} catch (Exception e) {
			logger.error("JMX", e);
		}
	}

	@Override
	public int getSchedulerQueueSize() {
		return scheduler.getSchedulerQueueSize();
	}

	@Override
	public int getPoolActiveCount() {
		return scheduler.getPoolActiveCount();
	}

}
