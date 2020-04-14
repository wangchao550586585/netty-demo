package nio.chapter6;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
/**
 * 代码清单 6-3 消费并释放入站消息
 */
@ChannelHandler.Sharable
public class C_DiscardInboundHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //释放资源
        ReferenceCountUtil.release(msg);
    }
}
