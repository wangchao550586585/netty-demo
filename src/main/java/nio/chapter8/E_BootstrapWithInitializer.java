package nio.chapter8;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;

import java.net.InetSocketAddress;

/**
 * 代码清单 8-6 引导和使用 ChannelInitializer
 * 一个必须要支持多种协议的应用程序将会有很多的ChannelHandler，而不会是一个庞大而又笨重的类。
 * 解决:
 * 通过在ChannelPipeline 中将它们链接在一起来部署尽可能多的ChannelHandler
 * 如果在引导的过程中你只能设置一个ChannelHandler,使用ChannelInitializer
 */
public class E_BootstrapWithInitializer {
    public void bootstrap() throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup()).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializerImpl());
        ChannelFuture future = serverBootstrap.bind(new InetSocketAddress(8080));
        future.sync();
    }

    final class ChannelInitializerImpl extends ChannelInitializer<Channel> {
        /**
         * 将所需的ChannelHandler添加到CHannelPipeline
         * 这个方法提供了一种将多个ChannelHandler 添加到一个ChannelPipeline 中的简便方法
         *在该方法返回之后，ChannelInitializer 的实例将会从Channel-Pipeline 中移除它自己
         * @param channel
         * @throws Exception
         */
        @Override
        protected void initChannel(Channel channel) throws Exception {
            ChannelPipeline pipeline = channel.pipeline();
            pipeline.addLast(new HttpClientCodec());
            pipeline.addLast(new HttpObjectAggregator(Integer.MAX_VALUE));

        }
    }
}
