package com.windforce.dispatcher;

import com.baidu.bjf.remoting.protobuf.ProtobufIDLGenerator;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.windforce.annotation.SocketClass;
import com.windforce.annotation.SocketMethod;
import com.windforce.annotation.SocketPacket;
import com.windforce.common.threadpool.AbstractDispatcherHashCodeRunable;
import com.windforce.common.threadpool.IdentityEventExecutorGroup;
import com.windforce.common.utility.FileUtils;
import com.windforce.core.SessionManager;
import com.windforce.core.WrequestPacket;
import com.windforce.core.WresponsePacket;
import com.windforce.core.Wsession;
import com.windforce.filter.firewall.FirewallManager;
import com.windforce.utils.IpUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息处理器handler
 *
 * @author Kuang Hao
 * @since v1.0 2016年6月3日
 */
@Sharable
@Component
@SuppressWarnings("unchecked")
public class SocketPacketHandler extends ChannelInboundHandlerAdapter implements BeanPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SocketPacketHandler.class);

    /**
     * 接收packet与方法的映射
     */
    private ConcurrentHashMap<SocketHandleKey, SocketHandlerDefinition> handlerDefinitions = new ConcurrentHashMap<SocketHandleKey, SocketHandlerDefinition>();

    /**
     * 类class与packetId的快速映射
     */
    private Map<Class<?>, SocketHandleKey> socketClassKeys = new ConcurrentHashMap<Class<?>, SocketHandleKey>();

    @Autowired
    private SessionManager sessionManager;

    private FirewallManager firewallManager;

    @Override
    public Object postProcessAfterInitialization(Object bean, String name) throws BeansException {
        Class<?> clazz = bean.getClass();
        if (clazz.isAnnotationPresent(SocketClass.class)) {
            for (Method method : clazz.getMethods()) {
                if (!method.isAnnotationPresent(SocketMethod.class)) {
                    continue;
                }

                // 参数和返回值验证
                Class<?>[] clzs = method.getParameterTypes();
                if (clzs.length != 2) {
                    throw new IllegalArgumentException(
                            bean.getClass().getName() + "." + method.getName() + "只能拥有两个参数。");
                }
                if (!Wsession.class.isAssignableFrom(clzs[0])) {
                    throw new IllegalArgumentException(bean.getClass().getName() + "." + method.getName()
                            + "第一个参数必须为[com.windforce.core.WSession]类型。");
                }
                if (clzs[1].getAnnotation(SocketPacket.class) == null) {
                    throw new IllegalArgumentException(
                            bean.getClass().getName() + "." + method.getName() + "第二个参数必须为包含SocketClass注解。");
                }
                if (method.getReturnType().getName() != "void") {
                    if (method.getReturnType().getAnnotation(SocketPacket.class) == null) {
                        throw new IllegalArgumentException(
                                bean.getClass().getName() + "." + method.getName() + "返回值必须包含SocketClass注解。");
                    }
                }

                // 接收参数验证
                Class<?> parameterPacketClass = method.getParameterTypes()[1];
                Class<?> returnPacketClass = method.getReturnType();
                for (Class<?> packetClass : new Class<?>[]{parameterPacketClass, returnPacketClass}) {
                    // 接收参数
                    if ("void".equals(packetClass.getName())) {
                        // 返回参数为void
                        continue;
                    }
                    SocketHandleKey key = socketClassKeys.get(packetClass);
                    if (key != null) {
                        // 该通信包已经注册过
                        throw new IllegalArgumentException(
                                String.format("接收参数[%s]已经注册过,但又尝试在[%s]中注册!", packetClass, bean.getClass()));
                    }
                    SocketPacket socketPacket = packetClass.getAnnotation(SocketPacket.class);
                    if (socketPacket == null) {
                        // 该对象没有class注册
                        throw new IllegalArgumentException(String.format("class[%s]没有包含SocketPacket注解!", packetClass));
                    }
                    key = SocketHandleKey.valueOf(socketPacket.packetId());
                    socketClassKeys.put(packetClass, key);
                    // 是否有其它执行对象已经注册了此消息
                    SocketHandlerDefinition shd = handlerDefinitions.get(key);
                    if (shd != null) {
                        throw new IllegalArgumentException(
                                String.format("class[%s]和class[%s],packetId[%s]重复使用,一个packetId只能用在一个方法上!",
                                        shd.getBean().getClass(), packetClass, socketPacket.packetId()));
                    }

                    try {
                        handlerDefinitions.put(key, SocketHandlerDefinition.valueOf(bean, method,
                                ProtobufProxy.create(packetClass, false, null)));
                        iDLGenerator(packetClass, key.getPacketId());
                    } catch (Throwable e) {
                        e.printStackTrace();
                        logger.error(String.format("初始化[%s]生成SocketHandlerDefinition失败!", packetClass), e);
                        throw new IllegalArgumentException("初始化生成SocketHandlerDefinition失败!");
                    }
                }
            }
        }

        return bean;
    }

    private void iDLGenerator(Class<?> clazz, short packetId) throws Exception {
        String idl = ProtobufIDLGenerator.getIDL(clazz, null, null, true);
        // TODO 配置
        File file = new File("D:\\proto\\" + packetId + "_" + clazz.getSimpleName() + ".proto");
        FileUtils.createFile(file);
        FileWriter fw = new FileWriter(file);
        fw.write(idl);
        fw.flush();
        fw.close();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String name) throws BeansException {
        return bean;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        final WrequestPacket packet = (WrequestPacket) msg;
        SocketHandleKey key = SocketHandleKey.valueOf(packet.getPacketId());
        final SocketHandlerDefinition shd = handlerDefinitions.get(key);
        if (shd == null) {
            logger.error(String.format("没有找到处理[%s]的SocketHandlerDefinition。", key));
            return;
        }
        final Channel channel = ctx.channel();
        final Object message = shd.getCodec().decode(packet.getData());

        final Wsession session = sessionManager.getSession(ctx.channel().id());
        if (!firewallManager.packetFilter(session, packet)) {
            logger.warn(String.format("session[%s] packetId[%s]发送非法的消息!", IpUtils.getIp(ctx.channel().localAddress()),
                    packet.getPacketId()));
        }
        IdentityEventExecutorGroup.addTask(new AbstractDispatcherHashCodeRunable() {

            @Override
            protected void doRun() {
                try {
                    Object returnMessage = shd.invoke(session, message);
                    if (returnMessage != null) {
                        SocketHandleKey socketHandleKey = socketClassKeys.get(returnMessage.getClass());
                        if (socketHandleKey != null) {
                            byte[] returnMessageBytes = handlerDefinitions.get(socketHandleKey).getCodec()
                                    .encode(returnMessage);
                            WresponsePacket wp = WresponsePacket.valueOf(socketHandleKey.getPacketId(),
                                    returnMessageBytes);
                            channel.writeAndFlush(wp);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("SocketHandlerDefinition任务执行失败!", e);
                }

            }

            @Override
            public long timeoutNanoTime() {
                // 3毫秒
                return 3 * 1000 * 1000;
            }

            @Override
            public String name() {
                return "wsocket_" + packet.getPacketId();
            }

            @Override
            public int getDispatcherHashCode() {
                return session.selectDispatcherHashCode();
            }
        });

        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("通信包异常!", cause);
        ctx.close();
    }

    public FirewallManager getFirewallManager() {
        return firewallManager;
    }

    public void setFirewallManager(FirewallManager firewallManager) {
        this.firewallManager = firewallManager;
    }

}
