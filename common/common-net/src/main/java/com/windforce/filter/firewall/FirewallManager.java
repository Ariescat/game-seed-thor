package com.windforce.filter.firewall;

import com.windforce.core.WrequestPacket;
import com.windforce.core.Wsession;

/**
 * 防火墙管理接口
 *
 * @author Kuang Hao
 * @since v1.0 2016-1-20
 */
public interface FirewallManager {

	/**
	 * 是否允许该消息通过
	 *
	 * @param session
	 * @param packet
	 * @return
	 */
	boolean packetFilter(Wsession session, WrequestPacket packet);

}
