package nio.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * 代码清单 10-4 TooLongFrameException
 *由于Netty 是一个异步框架，所以需要在字节可以解码之前在内存中缓冲它们。因此，不能
 让解码器缓冲大量的数据以至于耗尽可用的内存。为了解除这个常见的顾虑，Netty 提供了
 TooLongFrameException 类，其将由解码器在帧超出指定的大小限制时抛出。
 */
public class D_SafeByteToMessageDecoder extends ByteToMessageDecoder {
    private static final int MAX_FRAME_SIZE = 1024;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int readableBytes = byteBuf.readableBytes();
        //检查缓冲区中是否超过MAX_FRAME_SIZE个字节
        if (readableBytes > MAX_FRAME_SIZE) {
            //跳过所有的可读字节,抛出TooLongFrameException并通知ChannelHandler
            byteBuf.skipBytes(readableBytes);
            throw new TooLongFrameException("Frame too big");
        }
    }
}
