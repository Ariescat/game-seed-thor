package com.windforce.core;

import com.windforce.filter.firewall.FirewallRecord;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 会话
 *
 * @author Kuang Hao
 * @since v1.0 2016年6月3日
 */
public class Wsession {

	private static final AtomicInteger SEQ = new AtomicInteger(1);

	private int id;

	private Channel channel;

	private int dispatcherHashCode;

	/**
	 * 流量记录
	 */
	private FirewallRecord firewallRecord = new FirewallRecord();

	private Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();

	private Wsession() {
	}

	public static Wsession valueOf(Channel channel) {
		Wsession session = new Wsession();
		session.channel = channel;
		session.id = SEQ.incrementAndGet();
		return session;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public FirewallRecord getFirewallRecord() {
		return firewallRecord;
	}

	public void setFirewallRecord(FirewallRecord firewallRecord) {
		this.firewallRecord = firewallRecord;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 当dispatcherHashCode没有初始化时选择channel的hashcode作为分发code
	 *
	 * @return
	 */
	public int selectDispatcherHashCode() {
		if (dispatcherHashCode == 0) {
			return Math.abs(channel.hashCode());
		}
		return dispatcherHashCode;
	}

	public int getDispatcherHashCode() {
		return dispatcherHashCode;
	}

	public void setDispatcherHashCode(int dispatcherHashCode) {
		this.dispatcherHashCode = dispatcherHashCode;
	}

}
