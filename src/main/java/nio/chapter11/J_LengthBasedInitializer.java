package nio.chapter11;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 代码清单 11-10 使用 LengthFieldBasedFrameDecoder 解码器基于长度的协议
 *
 * FixedLengthFrameDecoder 提取在调用构造函数时指定的定长帧
 LengthFieldBasedFrameDecoder 根据编码进帧头部中的长度值提取帧；该字段的偏移量以及长度在构造函数中指定

 * @author WangChao
 * @create 2018/3/24 10:55
 */
public class J_LengthBasedInitializer extends ChannelInitializer<Channel>{
    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        //使用LengthFieldBasedFrameDecoder解码将帧长度编码到帧起始的前8个字节中的消息
        pipeline.addLast(new LengthFieldBasedFrameDecoder(64*1024,0,8));
        pipeline.addLast(new FrameHandler());
    }

    private class FrameHandler extends SimpleChannelInboundHandler<ByteBuf>{
        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {

        }
    }
}
