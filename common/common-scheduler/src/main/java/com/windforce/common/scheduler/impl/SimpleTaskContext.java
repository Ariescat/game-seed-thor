package com.windforce.common.scheduler.impl;

import com.windforce.common.scheduler.TaskContext;

import java.util.Date;

/**
 * 简单的任务上下文对象
 *
 * @author Frank
 */
public class SimpleTaskContext implements TaskContext {

	/**
	 * 计划执行时间
	 */
	private volatile Date lastScheduledTime;
	/**
	 * 实际执行时间
	 */
	private volatile Date lastActualTime;
	/**
	 * 实际完成时间
	 */
	private volatile Date lastCompletionTime;

	public void update(Date scheduledTime, Date actualTime, Date completionTime) {
		this.lastScheduledTime = scheduledTime;
		this.lastActualTime = actualTime;
		this.lastCompletionTime = completionTime;
	}

	public Date lastScheduledTime() {
		return this.lastScheduledTime;
	}

	public Date lastActualTime() {
		return this.lastActualTime;
	}

	public Date lastCompletionTime() {
		return this.lastCompletionTime;
	}

}
