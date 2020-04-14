package nio.chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;

import java.net.InetSocketAddress;

/**
 * 代码清单 8-9 优雅关闭
 *
 *

 也可以在调用EventLoopGroup.shutdownGracefully()方法之前，显式地
 在所有活动的Channel 上调用Channel.close()方法。但是在任何情况下，都请记得关闭
 EventLoopGroup 本身。
 * @author WangChao
 * @create 2018/3/17 7:43
 */
public class H_GracefulShutdown {
    public void bootstrap() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class)
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {

                    }
                });
        bootstrap.connect(new InetSocketAddress("www.com", 80)).syncUninterruptibly();
        //shutdownGracefully:释放所有的资源,且关闭所有正在使用中的channel
       /* 这个方法调用将会返回一个Future，这个Future 将在关闭完成时接收到通知。需要注意的是，
        shutdownGracefully()方法也是一个异步的操作，所以你需要阻塞等待直到它完成，或者向
        所返回的Future 注册一个监听器以在关闭完成时获得通知。*/
        Future<?> future = group.shutdownGracefully();
        future.syncUninterruptibly();

    }
}
