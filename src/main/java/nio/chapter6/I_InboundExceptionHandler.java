package nio.chapter6;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 *代码清单 6-12 基本的入站异常处理
 */
public class I_InboundExceptionHandler extends ChannelInboundHandlerAdapter{
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        //关闭了Channel 并打印了异常的栈跟踪信息。
        ctx.close();
    }
}
