package nio.chapter9;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * 代码清单9-5 FrameChunkDecoder
 */
//扩展 ByteToMessageDecoder以将入站字节解码为消息
public class C_FrameChunkDecoder extends ByteToMessageDecoder {
    private final int maxFrameSize;
    //指定将要产生的帧的最大允许大小
    public C_FrameChunkDecoder(int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
    }


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int readableBytes = byteBuf.readableBytes();
        if (readableBytes > maxFrameSize) {
            //如果帧大,则丢弃并抛异常
            byteBuf.clear();
            throw new TooLongFrameException();
        }
        //否则,从ByteBuf读取一个新帧
        ByteBuf buf = byteBuf.readBytes(readableBytes);
        //将该帧添加到解码消息的 List 中
        list.add(buf);
    }
}
