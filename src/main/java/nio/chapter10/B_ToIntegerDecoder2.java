package nio.chapter10;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * 代码清单 10-2 ToIntegerDecoder2 类扩展了 ReplayingDecoder
 * 扩展 ReplayingDecoder<Void> 以将字节解码为消息
 * 类型参数S 指定了用于状态管理的类型，其中Void 代表不需要状态管理。
 * <p>
 * 使得我们不必调用readableBytes()方法(如: if (byteBuf.readableBytes() >= 4) {} )。它通过使用一个自定义的ByteBuf实现，
 * ReplayingDecoderByteBuf，包装传入的ByteBuf实现了这一点，其将在内部(指readableBytes#方法)执行该调用
 */
public class B_ToIntegerDecoder2 extends ReplayingDecoder<Void> {
    /**
     *
     * @param channelHandlerContext
     * @param byteBuf 传入的是ReplayingDecoderByteBuf
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //从入站byteBuf读取int,添加到解码消息的List中
        list.add(byteBuf.readInt());
    }
}
