package com.windforce.common.profile.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 性能记录信息
 *
 * @author Ariescat
 * @version 2020/11/13 15:58
 */
public class Profile<PK, Column extends Enum<Column> & IProColumn> {

	/**
	 * 记录信息<数据Key，数据>
	 */
	private final Map<PK, ProRowInfo<PK, Column>> rowInfoMap = new ConcurrentHashMap<>();
	/**
	 * 统计开始时间
	 */
	private long startTime = System.currentTimeMillis();
	/**
	 * 统计时长
	 */
	private long totalTime;
	/**
	 * 需要记录字段的enum字段
	 */
	private Column[] columns;

	public Profile(Class<Column> columnClass) {
		this(columnClass.getEnumConstants());
	}

	private Profile(Column[] columns) {
		this.columns = columns;
	}

	/**
	 * 重置统计
	 */
	public void reset() {
		this.rowInfoMap.clear();
		this.startTime = System.currentTimeMillis();
		this.totalTime = 0;
	}

	/**
	 * 创建一行记录数据
	 * ProRow
	 * .add(Column column, long value)
	 * .add(Column column, long value)
	 * .add(Column column, long value)
	 * .submit();
	 *
	 * @param pk 数据key
	 * @return 填充完数据后，可以使用{@link ProRow#submit()}快截提交
	 */
	public ProRow createRow(PK pk) {
		return new ProRow(pk);
	}

	public void addRow(ProRow proRow) {
		rowInfoMap
			.computeIfAbsent(proRow.pk, pk -> new ProRowInfo<>(pk, columns))
			.record(proRow);
	}

	/**
	 * 获取统计持续时长
	 * 如果在调用end之前调用该方法，则意味着统计还未结束，则使用当前时间作为统计结束时间用于计算时长
	 *
	 * @param timeUnit 返回时间单位
	 * @return 持续时长
	 */
	public long getDuration(TimeUnit timeUnit) {
		return timeUnit.convert(
			(this.totalTime == 0 ? System.currentTimeMillis() - this.startTime : this.totalTime),
			TimeUnit.MILLISECONDS
		);
	}

	public long getStartTime() {
		return startTime;
	}

	public List<ProRowInfo<PK, Column>> cover2List() {
		return new ArrayList<>(rowInfoMap.values());
	}

	/**
	 * 获取当前统计的数据集合
	 * <p>
	 * !!important 注意线程安全
	 */
	public Map<PK, ProRowInfo<PK, Column>> getRowInfoMap() {
		return rowInfoMap;
	}

	/**
	 * 性能单行数据
	 */
	public class ProRow {
		/**
		 * 数据Key
		 */
		private final PK pk;
		/**
		 * 单行详细信息
		 */
		private final long[] row = new long[columns.length];

		private ProRow(PK pk) {
			this.pk = pk;
		}

		/**
		 * 添加数据
		 *
		 * @param column 数据字段
		 * @param value  值
		 */
		public ProRow add(Column column, long value) {
			row[column.ordinal()] = column.getColumnUnit().transferValue(value);
			return this;
		}

		public ProRow add1(Column column) {
			return add(column, 1);
		}

		/**
		 * 提交记录
		 */
		public void submit() {
			addRow(this);
		}

		public PK getPk() {
			return pk;
		}

		public long[] getRow() {
			return row;
		}

		public long getValue(Column column) {
			return row[column.ordinal()];
		}
	}
}
