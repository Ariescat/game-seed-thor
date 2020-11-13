package com.test.time.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.windforce.annotation.SocketPacket;

@SocketPacket(packetId = 1)
public class CM_Req_Time {
	@Protobuf
	private int nil;
	@Protobuf
	private InnerObject innerObject;

	public int getNil() {
		return nil;
	}

	public void setNil(int nil) {
		this.nil = nil;
	}

	public InnerObject getInnerObject() {
		return innerObject;
	}

	public void setInnerObject(InnerObject innerObject) {
		this.innerObject = innerObject;
	}

}
