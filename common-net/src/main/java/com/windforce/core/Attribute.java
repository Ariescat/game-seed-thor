package com.windforce.core;

/**
 * 属性字段
 *
 * @author Kuang Hao
 * @since v1.0 2016年6月3日
 */
public enum Attribute {
	/**
	 * 玩家登陆认证
	 */
	IDENTITY("IDENTITY"),

	/**
	 * 管理后台认证
	 */
	MANAGEMENT("MANAGEMENT");

	private final String name;

	private Attribute(String name) {
		this.name = name;
	}

	public boolean hasAttr(Wsession session) {
		return session.getAttributes().containsKey(this.getName());
	}

	public String getName() {
		return name;
	}

}
