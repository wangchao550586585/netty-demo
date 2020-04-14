package nio.chapter11;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * 代码清单 11-8 处理由行尾符分隔的帧
 * 解码基于分隔符的协议和基于长度的协议
 */
public class H_LineBasedHandlerInitializer extends ChannelInitializer<Channel>{
    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        //将提取的帧转发给下一个ChannelInboundHandler
        pipeline.addLast(new LineBasedFrameDecoder(64*1024));
        //添加FrameHandler以接收帧
        pipeline.addLast(new FrameHandler());
    }

    private static final class FrameHandler extends SimpleChannelInboundHandler<ByteBuf> {
        @Override
        //传入单个帧的内容
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {

        }
    }
}
