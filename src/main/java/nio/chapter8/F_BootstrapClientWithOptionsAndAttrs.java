package nio.chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;
import java.security.Guard;

/**
 * 代码清单 8-7 使用属性值
 * 如何使用ChannelOption 来配置Channel，以及如果使用属性来存储整型值。
 *
 * Netty 提供了
 AttributeMap 抽象（一个由Channel 和引导类提供的集合）以及AttributeKey<T>（一
 个用于插入和获取属性值的泛型类）。使用这些工具，便可以安全地将任何类型的数据项与客户
 端和服务器Channel（包含ServerChannel 的子Channel）相关联了。

 * @author WangChao
 * @create 2018/3/17 7:06
 */
public class F_BootstrapClientWithOptionsAndAttrs {
    public void bootstrap() {
        //创建一个 AttributeKey 以标识该属性
        final AttributeKey<Integer> id = AttributeKey.newInstance("ID");
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                        //使用AttributeKey检索属性以及它的值
                        Integer idValue = ctx.channel().attr(id).get();
                    }

                    @Override
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                        System.out.println("Received data");
                    }
                });
        //设置channelOption,其将在connect/bind被调用时被设置到已经创建的channel上
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.SO_TIMEOUT, 5000);
        //存储该id属性
        bootstrap.attr(id, 123456);
        ChannelFuture future = bootstrap.connect(new InetSocketAddress("wwww.aa.com", 80));
        future.syncUninterruptibly();
    }
}
