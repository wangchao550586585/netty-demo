package nio.chapter11;

import io.netty.channel.*;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

import java.io.Serializable;

/**
 * 代码清单 11-13 使用 JBoss Marshalling
 * 序列化
 *
 * CompatibleObjectDecoder(过时) 和使用 JDK 序列化的非基于 Netty的远程节点进行互操作的解码器
     CompatibleObjectEncoder 和使用JDK 序列化的非基于Netty 的远程节点进行互操作的编码器
     ObjectDecoder 构建于JDK 序列化之上的使用自定义的序列化来解码的解码器；当
     没有其他的外部依赖时，它提供了速度上的改进。否则其他的序列化
     实现更加可取
     ObjectEncoder 构建于JDK 序列化之上的使用自定义的序列化来编码的编码器；当
     没有其他的外部依赖时，它提供了速度上的改进。否则其他的序列化
     实现更加可取

 CompatibleMarshallingDecoder
 CompatibleMarshallingEncoder与只使用JDK 序列化的远程节点兼容

 MarshallingDecoder
 MarshallingEncoder:适用于使用JBoss Marshalling 的节点。这些类必须一起使用
 * @author WangChao
 * @create 2018/3/24 12:09
 */
public class M_MarshallingInitializer extends ChannelInitializer<Channel> {
    private final MarshallerProvider marshallerProvider;
    private final UnmarshallerProvider unmarshallerProvider;

    public M_MarshallingInitializer(MarshallerProvider marshallerProvider, UnmarshallerProvider unmarshallerProvider) {
        this.marshallerProvider = marshallerProvider;
        this.unmarshallerProvider = unmarshallerProvider;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        //添加MarshallingDecoder以将ByteBuf转换为POJO
        pipeline.addLast(new MarshallingDecoder(unmarshallerProvider));
        //添加MarshallingEncoder以将POJO转换为ByteBuf
        pipeline.addLast(new MarshallingEncoder(marshallerProvider));
        //添加ObjectHandler以处理普通的实现了Serializable接口的pojo
        pipeline.addLast(new ObjectHandler());
    }

    private class ObjectHandler extends SimpleChannelInboundHandler<Serializable>{

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, Serializable serializable) throws Exception {

        }
    }
}
