package nio.chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.oio.OioSocketChannel;

import java.net.InetSocketAddress;

/**
 * 代码清单 8-3 不兼容的 Channel 和 EventLoopGroup
 *
 * channel
     ├───nio
     │      NioEventLoopGroup
     ├───oio
     │      OioEventLoopGroup
     └───socket
         ├───nio
         │   NioDatagramChannel
         │   NioServerSocketChannel
         │   NioSocketChannel
         └───oio
             OioDatagramChannel
             OioServerSocketChannel
             OioSocketChannel

 在引导的过程中，在调用bind()或者connect()方法之前，必须调用以下方法来设置所需的组件：
  group()；
  channel()或者channelFactory()；
  handler()。
 如果不这样做，则将会导致IllegalStateException。对handler()方法的调用尤其重要，因
 为它需要配置好ChannelPipeline。
 */
public class B_InvalidBootstrapClient {
    public static void main(String[] args) {
        B_InvalidBootstrapClient b_invalidBootstrapClient = new B_InvalidBootstrapClient();
        b_invalidBootstrapClient.bootstrap();
    }

    public void bootstrap() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                //指定一个适用于 NIO 的 EventLoopGroup 实现
                .group(group)
                //指定一个适用于 OIO 的 Channel 实现类
                .channel(OioSocketChannel.class)
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
            @Override
            protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                System.out.println("Received data");
            }
        });
        ChannelFuture future = bootstrap.connect(new InetSocketAddress("www.manning.com", 80));
        future.syncUninterruptibly();
    }
}
