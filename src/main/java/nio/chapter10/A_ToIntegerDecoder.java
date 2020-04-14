package nio.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
/**
 * 代码清单 10-1 ToIntegerDecoder 类扩展了 ByteToMessageDecoder
 */
//扩展ByteToMessageDecoder类，以将字节解码为特定的格式
public class A_ToIntegerDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() >= 4) {
            list.add(byteBuf.readInt());
        }
    }
}
