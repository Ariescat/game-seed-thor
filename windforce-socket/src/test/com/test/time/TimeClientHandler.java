package com.test.time;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.test.time.packet.CM_Req_Time;
import com.test.time.packet.InnerObject;
import com.test.time.packet.TimePacket;
import com.windforce.core.WrequestPacket;
import com.windforce.core.WresponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;
import java.util.Date;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    private WrequestPacket firstPacket;

    public TimeClientHandler() throws IOException {
        Codec<CM_Req_Time> cmCodec = ProtobufProxy.create(CM_Req_Time.class);
        CM_Req_Time cm = new CM_Req_Time();
        cm.setInnerObject(new InnerObject());
        cm.getInnerObject().setName("account_idx");
        byte[] data = cmCodec.encode(cm);
        firstPacket = WrequestPacket.valueOf((short) 1, data);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(firstPacket);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        WresponsePacket packet = (WresponsePacket) msg;
        Codec<TimePacket> codec = ProtobufProxy.create(TimePacket.class);
        TimePacket time = codec.decode(packet.getData());
        System.out.println("当前系统时间为:" + new Date(time.getTime()));
        ctx.channel().close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // ctx.close();
    }

}
