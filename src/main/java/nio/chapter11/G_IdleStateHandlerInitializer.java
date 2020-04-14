package nio.chapter11;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 空闲的连接和超时
 * 代码清单 11-7 发送心跳
     * IdleStateHandler 当连接空闲时间太长时，将会触发一个IdleStateEvent 事件。然后，
                         你可以通过在你的ChannelInboundHandler 中重写userEvent-
                        Triggered()方法来处理该IdleStateEvent 事件
     ReadTimeoutHandler 如果在指定的时间间隔内没有收到任何的入站数据，则抛出一个Read-
                        TimeoutException 并关闭对应的Channel。可以通过重写你的
                        ChannelHandler 中的exceptionCaught()方法来检测该Read-
                        TimeoutException
     WriteTimeoutHandler 如果在指定的时间间隔内没有任何出站数据写入，则抛出一个Write-
                        TimeoutException 并关闭对应的Channel 。可以通过重写你的
                        ChannelHandler 的exceptionCaught()方法检测该WriteTimeout-
                        Exception

 如果连接超过60 秒没有接收或者发送任何的数据，那么IdleStateHandler 将会使用一个
 IdleStateEvent 事件来调用fireUserEventTriggered()方法。HeartbeatHandler 实现
 了userEventTriggered()方法，如果这个方法检测到IdleStateEvent 事件，它将会发送心
 跳消息，并且添加一个将在发送操作失败时关闭该连接的ChannelFutureListener
 */
public class G_IdleStateHandlerInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        //(1) IdleStateHandler 将在被触发时发送一个IdleStateEvent 事件
        pipeline.addLast(new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS));
        //将一个 HeartbeatHandler 添加到ChannelPipeline中
        pipeline.addLast(new HeartbeatHandler());
    }

    //实现userEventTriggered()发送心跳消息
    private static final class HeartbeatHandler extends ChannelInboundHandlerAdapter {
        //发送到远程节点的心跳信息
        private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("HEARTBEAT", CharsetUtil.ISO_8859_1));

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            //(2) 发送心跳消息，并在发送失败时关闭该连接
            if (evt instanceof IdleStateEvent) {
                ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } else {
                //不是IdleStateEvent事件,所以将它传递给下一个ChannelInboundHandler
                super.userEventTriggered(ctx, evt);
            }
        }
    }
}
