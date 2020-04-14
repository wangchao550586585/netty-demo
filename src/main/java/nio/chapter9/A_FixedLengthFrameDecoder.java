package nio.chapter9;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/** 代码清单9-1 FixedLengthFrameDecoder
 * 扩展 ByteToMessageDecoder 以处理入站字节，并将它们解码为消息
 * @author WangChao
 * @create 2018/3/19 8:13
 */
public class A_FixedLengthFrameDecoder extends ByteToMessageDecoder {
    private final int frameLength;
    //指定要生成的帧的长度
    public A_FixedLengthFrameDecoder(int frameLength) {
        if (frameLength <= 0) {
            throw new IllegalArgumentException(
                    "frameLength must be a positive integer: " + frameLength);
        }
        this.frameLength = frameLength;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //检查是否有足够的字节可以被读取，以生成下一个帧
        while (byteBuf.readableBytes() >= frameLength) {
            //从 ByteBuf 中读取一个新帧
            ByteBuf buf = byteBuf.readBytes(frameLength);
            //将该帧添加到已被解码的消息列表中
            list.add(buf);
        }
    }
}
