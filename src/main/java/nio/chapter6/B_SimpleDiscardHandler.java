package nio.chapter6;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
/**
 * 代码清单 6-2 使用 SimpleChannelInboundHandler
 */
@ChannelHandler.Sharable
public class B_SimpleDiscardHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
            //不需要任何显式的资源释放
    }
}
