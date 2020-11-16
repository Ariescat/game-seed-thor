package com.windforce.common.threadpool;

import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.ScheduledFuture;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Kuang Hao
 * @since v1.0 2018年2月3日
 */
class EventExecutor extends SingleThreadEventExecutor {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(EventExecutor.class);

	/**
	 * 统计各种类型的任务执行情况
	 */
	private ConcurrentHashMap<String, RunableStatistics> runableStatistics = new ConcurrentHashMap<>();

	protected EventExecutor(EventExecutorGroup parent, ThreadFactory threadFactory, boolean addTaskWakesUp) {
		super(parent, threadFactory, addTaskWakesUp);
	}

	@Override
	protected void run() {
		for (; ; ) {
			Runnable task = takeTask();
			if (task != null) {
				task.run();
				updateLastExecutionTime();
			}
			if (confirmShutdown()) {
				break;
			}
		}
	}

	public ScheduledFuture<?> addScheduleTask(AbstractDispatcherHashCodeRunable task, long delay, TimeUnit unit) {
		statistics(task);
		return schedule(task, delay, unit);
	}

	public ScheduledFuture<?> addScheduleAtFixedRate(AbstractDispatcherHashCodeRunable task, long initialDelay,
													 long period, TimeUnit unit) {
		return scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				addTask(task);
			}
		}, initialDelay, period, unit);

	}

	/**
	 * 统计
	 *
	 * @param task
	 */
	private void statistics(AbstractDispatcherHashCodeRunable task) {
		synchronized (runableStatistics) {
			if (!runableStatistics.containsKey(task.name())) {
				runableStatistics.put(task.name(), RunableStatistics.valueOf(task.name()));
			}
		}
		RunableStatistics rs = runableStatistics.get(task.name());
		rs.getCurrentCount().incrementAndGet();
		rs.getHistoryCount().incrementAndGet();
	}

	public Future<?> addTask(AbstractDispatcherHashCodeRunable task) {
		statistics(task);
		return submit(task);
	}

	public ConcurrentHashMap<String, RunableStatistics> getRunableStatistics() {
		return runableStatistics;
	}

	public void setRunableStatistics(ConcurrentHashMap<String, RunableStatistics> runableStatistics) {
		this.runableStatistics = runableStatistics;
	}

	/**
	 * 当类型任务统计
	 *
	 * @author Kuang Hao
	 * @since v1.0 2018年2月3日
	 */
	static class RunableStatistics {
		private String name;
		private AtomicLong currentCount = new AtomicLong(0);
		private AtomicLong timeOutCount = new AtomicLong(0);
		private AtomicLong historyCount = new AtomicLong(0);
		private AtomicLong exeTotalTime = new AtomicLong(0);

		public static RunableStatistics valueOf(String name) {
			RunableStatistics rs = new RunableStatistics();
			rs.name = name;
			return rs;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public AtomicLong getCurrentCount() {
			return currentCount;
		}

		public void setCurrentCount(AtomicLong currentCount) {
			this.currentCount = currentCount;
		}

		public AtomicLong getTimeOutCount() {
			return timeOutCount;
		}

		public void setTimeOutCount(AtomicLong timeOutCount) {
			this.timeOutCount = timeOutCount;
		}

		public AtomicLong getHistoryCount() {
			return historyCount;
		}

		public void setHistoryCount(AtomicLong historyCount) {
			this.historyCount = historyCount;
		}

		public AtomicLong getExeTotalTime() {
			return exeTotalTime;
		}

		public void setExeTotalTime(AtomicLong exeTotalTime) {
			this.exeTotalTime = exeTotalTime;
		}

	}

}
