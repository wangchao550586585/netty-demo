package nio.chapter11;

import com.google.protobuf.MessageLite;
import io.netty.channel.*;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * 代码清单 11-14 使用 protobuf
 * ProtobufDecoder 使用protobuf 对消息进行解码
 ProtobufEncoder 使用protobuf 对消息进行编码
 ProtobufVarint32FrameDecoder 根据消息中的Google Protocol Buffers 的“Base 128 Varints”a
                            整型长度字段值动态地分割所接收到的ByteBuf
 ProtobufVarint32LengthFieldPrepender 向ByteBuf 前追加一个Google Protocal Buffers 的“Base128 Varints”
                            整型的长度字段值
 * @author WangChao
 * @create 2018/3/24 12:18
 */
public class N_ProtoBufInitializer extends ChannelInitializer<Channel> {
    private final MessageLite lite;

    public N_ProtoBufInitializer(MessageLite lite) {
        this.lite = lite;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        //添加 ProtobufVarint32FrameDecoder 以分隔帧
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        //编码进帧长度信息
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        //添加 ProtobufEncoder 以处理消息的编码
        pipeline.addLast(new ProtobufEncoder());
        //添加 ProtobufDecoder 以解码消息
        pipeline.addLast(new ProtobufDecoder(lite));
        //添加 ObjectHandler 以处理解码消息
        pipeline.addLast(new ObjectHandler());
    }

    private class ObjectHandler extends SimpleChannelInboundHandler<Object> {
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

        }
    }
}
