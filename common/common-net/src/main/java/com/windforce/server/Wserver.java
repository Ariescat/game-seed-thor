package com.windforce.server;

import com.windforce.coder.WpacketDecoder;
import com.windforce.coder.WpacketEncoder;
import com.windforce.config.ServerConfig;
import com.windforce.config.ServerConfigConstant;
import com.windforce.dispatcher.SocketPacketHandler;
import com.windforce.filter.firewall.DummyFirewallManager;
import com.windforce.filter.firewall.FirewallManager;
import com.windforce.filter.firewall.FlowFirewall;
import com.windforce.filter.firewall.IpFirewall;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Socket服务器
 *
 * @author Kuang Hao
 * @since v1.0 2016年6月3日
 */
@Component
public class Wserver {

	private static final Logger logger = LoggerFactory.getLogger(Wserver.class);

	@Autowired
	private CustomHandlerManager customHandlerManager;

	private int[] ports;

	@Autowired
	private SessionHandler sessionHandler;

	@Autowired
	private FlowFirewall flowFirewall;

	@Autowired
	private IpFirewall ipFirewall;

	@Autowired
	private SocketPacketHandler socketPacketHandler;

	@Autowired
	private ServerConfig serverConfig;

	private int maxLength;
	private List<ChannelFuture> channelFutures = new ArrayList<>();
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;

	/**
	 * 开启
	 *
	 * @return
	 */
	public void open() {
		ipFirewall.open();
	}

	/**
	 * 关闭
	 *
	 * @return
	 */
	public void close() {
		ipFirewall.close();
	}

	@PostConstruct
	public void initProperties() throws IOException {
		String[] portstr = serverConfig.getProp(ServerConfigConstant.KEY_ADDRESS).split(",");
		ports = new int[portstr.length];
		int i = 0;
		for (String port : portstr) {
			ports[i] = Integer.valueOf(port);
			i++;
		}

		String maxLengthProp = serverConfig.getProp(ServerConfigConstant.PACKET_MAXLENGTH);
		if (maxLengthProp == null) {
			// 默认1m
			maxLength = 1024 * 1024;
		} else {
			maxLength = Integer.valueOf(maxLengthProp) * 1024;
		}
	}

	public void bind() throws InterruptedException, IOException {
		bind(new DummyFirewallManager());
	}

	public void bind(FirewallManager firewallManager) throws InterruptedException, IOException {
		socketPacketHandler.setFirewallManager(firewallManager);
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
			.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
			.childOption(ChannelOption.SO_BACKLOG, 1024).childOption(ChannelOption.SO_RCVBUF, 1024 * 32)
			.childOption(ChannelOption.SO_SNDBUF, 1024 * 32).handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel sc) throws Exception {
					sc.pipeline().addLast("session", sessionHandler);
					sc.pipeline().addLast("flowFirewall", flowFirewall);
					// sc.pipeline().addLast("ipFirewall", ipFirewall);
					sc.pipeline().addLast("decoder", new WpacketDecoder(maxLength));
					sc.pipeline().addLast("socketPacketHandler", socketPacketHandler);
					sc.pipeline().addLast("encoder", new WpacketEncoder());
					for (ChannelHandlerAdapter cha : customHandlerManager.getHandlers()) {
						sc.pipeline().addLast(cha);
					}
				}
			});

		for (int port : ports) {
			ChannelFuture cf = serverBootstrap.bind(port);
			cf.sync();
			channelFutures.add(cf);
		}
	}

	public void shutdownGracefully() {
		try {
			for (ChannelFuture cf : channelFutures) {
				if (cf.channel() != null) {
					try {
						cf.channel().close();
					} catch (Exception e) {
						logger.error("通信server channel关闭异常", e);
					}
				}
			}
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

}
