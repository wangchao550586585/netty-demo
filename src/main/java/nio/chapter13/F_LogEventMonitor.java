package nio.chapter13;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * 代码清单 13-8 LogEventMonitor
 *
 * @author WangChao
 * @create 2018/4/14 14:07
 */
public class F_LogEventMonitor {
    private final EventLoopGroup group;
    private final Bootstrap bootstrap;

    public F_LogEventMonitor(InetSocketAddress address) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        //引导该 NioDatagramChannel
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                //设置套接字选项 SO_BROADCAST
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel)
                            throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        //将 LogEventDecoder 和 LogEventHandler 添加到 ChannelPipeline 中
                        pipeline.addLast(new D_LogEventDecoder());
                        pipeline.addLast(new E_LogEventHandler());
                    }
                })
                .localAddress(address);
    }

    public Channel bind() {
        //绑定 Channel。注意，DatagramChannel 是无连接的
        return bootstrap.bind().syncUninterruptibly().channel();
    }

    public void stop() {
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        //构造一个新的 LogEventMonitor
        F_LogEventMonitor monitor = new F_LogEventMonitor(
                new InetSocketAddress(4096));
        try {
            Channel channel = monitor.bind();
            System.out.println("LogEventMonitor running");
            channel.closeFuture().sync();
        } finally {
            monitor.stop();
        }
    }
}
