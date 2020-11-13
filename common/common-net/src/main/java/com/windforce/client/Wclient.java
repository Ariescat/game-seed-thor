package com.windforce.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * socket异步客户端
 *
 * @author Kuang Hao
 * @since v1.0 2016年6月3日
 */
public class Wclient {

	private static EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

	private static Bootstrap b = new Bootstrap();

	private List<ChannelHandler> extraHandlers = new ArrayList<ChannelHandler>();

	public static void main(String[] args) {
		System.out.println(StringUtil.toHexString(new byte[]{5, 2, 3, 3, 4}));
		System.out.println(StringUtil.byteToHexString(15));
	}

	public void addChannelHandler(ChannelHandler ch) {
		extraHandlers.add(ch);
	}

	public ChannelFuture connect(int port, String host) throws InterruptedException {
		b.group(eventLoopGroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel sc) throws Exception {
					sc.pipeline().addLast("decoder", new WclientPacketDecoder());
					sc.pipeline().addLast("encoder", new WclientPacketEncoder());
					for (ChannelHandler ch : extraHandlers) {
						sc.pipeline().addLast(ch);
					}
				}
			});
		return b.connect(host, port).sync();
	}

	public void shutdownGracefully() {
		eventLoopGroup.shutdownGracefully();
	}
}
