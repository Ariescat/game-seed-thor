package com.windforce.filter.firewall;

import com.windforce.core.WrequestPacket;
import com.windforce.core.Wsession;

public class DummyFirewallManager implements FirewallManager {

	@Override
	public boolean packetFilter(Wsession session, WrequestPacket packet) {
		return true;
	}

}
