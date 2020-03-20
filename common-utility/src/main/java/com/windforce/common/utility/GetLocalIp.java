package com.windforce.common.utility;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 获取本机IP的程序
 *
 * @author hanchao
 */
public class GetLocalIp {

	/**
	 * 知识的补充
	 *
	 * InetAddress 继承自 java.lang.Object类 它有两个子类：Inet4Address 和 Inet6Address
	 * 此类表示互联网协议 (IP) 地址。
	 *
	 * IP 地址是 IP 使用的 32 位或 128 位无符号数字， 它是一种低级协议，UDP 和 TCP 协议都是在它的基础上构建的。
	 *
	 * ************************************************
	 * 主机名就是计算机的名字（计算机名），网上邻居就是根据主机名来识别的。 这个名字可以随时更改，从我的电脑属性的计算机名就可更改。
	 * 用户登陆时候用的是操作系统的个人用户帐号，这个也可以更改， 从控制面板的用户界面里改就可以了。这个用户名和计算机名无关。
	 */

	/**
	 * 获取本机的IP
	 *
	 * @return Ip地址
	 */
	public static String getLocalHostIP() {
		String ip;
		try {
			/** 返回本地主机。 */
			InetAddress addr = InetAddress.getLocalHost();
			/** 返回 IP 地址字符串（以文本表现形式） */
			ip = addr.getHostAddress();
		} catch (Exception ex) {
			ip = "";
		}

		return ip;
	}

	/**
	 * 或者主机名：
	 *
	 * @return
	 */
	public static String getLocalHostName() {
		String hostName;
		try {
			/** 返回本地主机。 */
			InetAddress addr = InetAddress.getLocalHost();
			/** 获取此 IP 地址的主机名。 */
			hostName = addr.getHostName();
		} catch (Exception ex) {
			hostName = "";
		}

		return hostName;
	}

	/**
	 * 获得本地所有的IP地址
	 *
	 * @return
	 */
	public static String[] getAllLocalHostIP() {

		String[] ret = null;
		try {
			/** 获得主机名 */
			String hostName = getLocalHostName();
			if (hostName.length() > 0) {
				/** 在给定主机名的情况下，根据系统上配置的名称服务返回其 IP 地址所组成的数组。 */
				InetAddress[] addrs = InetAddress.getAllByName(hostName);
				if (addrs.length > 0) {
					ret = new String[addrs.length];
					for (int i = 0; i < addrs.length; i++) {
						/** .getHostAddress() 返回 IP 地址字符串（以文本表现形式）。 */
						ret[i] = addrs[i].getHostAddress();
					}
				}
			}

		} catch (Exception ex) {
			ret = null;
		}

		return ret;
	}

	public static int ip2Int(String ip) throws UnknownHostException {
		InetAddress address = InetAddress.getByName(ip);// 在给定主机名的情况下确定主机的 IP 址。
		byte[] bytes = address.getAddress();// 返回此 InetAddress 对象的原始 IP 地址
		int a, b, c, d;
		a = byte2int(bytes[0]);
		b = byte2int(bytes[1]);
		c = byte2int(bytes[2]);
		d = byte2int(bytes[3]);
		int result = (a << 24) | (b << 16) | (c << 8) | d;
		return result;
	}

	public static int byte2int(byte b) {
		int l = b & 0x07f;
		if (b < 0) {
			l |= 0x80;
		}
		return l;
	}

	public static void main(String[] args) throws UnknownHostException {
		System.out.println("本机IP：" + getLocalHostIP());
		System.out.println("本机IPtoInt：" + ip2Int(getLocalHostIP()));
		System.out.println("本地主机名字为：" + getLocalHostName());

		String[] localIP = getAllLocalHostIP();
		for (int i = 0; i < localIP.length; i++) {
			System.out.println(localIP[i]);
		}

		InetAddress baidu = InetAddress.getByName("www.baidu.com");
		System.out.println("baidu : " + baidu);
		System.out.println("baidu IP: " + baidu.getHostAddress());
		System.out.println("baidu HostName: " + baidu.getHostName());
	}

}
