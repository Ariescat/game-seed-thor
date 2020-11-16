package com.windforce.common.profile.core;

import java.util.concurrent.atomic.AtomicLongArray;

/**
 * @author Ariescat
 * @version 2020/11/13 13:19
 */
public class ProRowInfo<PK, Column extends Enum<Column> & IProColumn> {
	private final PK key;

	private final IProStrategy[] strategies;
	/**
	 * 数据
	 */
	private final AtomicLongArray columnValues;

	ProRowInfo(PK key, Column[] columns) {
		this.key = key;
		this.columnValues = new AtomicLongArray(columns.length);
		this.strategies = new IProStrategy[columns.length];
		for (int i = 0; i < columns.length; i++) {
			strategies[i] = columns[i].getStrategy();
		}
	}

	public void record(Profile<?, Column>.ProRow proRow) {
		// 增加记录次数
		long[] row = proRow.getRow();
		for (int i = 0; i < row.length; i++) {
			long newValue = row[i];
			IProStrategy strategy = strategies[i];
			columnValues.getAndUpdate(i, operand -> strategy.reAdd(operand, newValue));
		}
	}

	/**
	 * 获取字段统计数据
	 *
	 * @param column 字段
	 * @return 如果无统计数据，返回0
	 */
	public long getValue(Column column) {
		return columnValues.get(column.ordinal());
	}

	public PK getKey() {
		return key;
	}
}
