package nio.chapter6;

import io.netty.channel.*;

/**
 * 处理出站异常2
 * 代码清单 6-14 添加 ChannelFutureListener 到 ChannelPromise
 */
public class I_OutboundExceptionHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        promise.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (!channelFuture.isSuccess()){
                    channelFuture.cause().printStackTrace();
                    channelFuture.channel().close();
                }
            }
        });
    }
}
