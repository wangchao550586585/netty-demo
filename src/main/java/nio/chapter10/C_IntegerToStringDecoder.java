package nio.chapter10;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * 代码清单 10-3 IntegerToStringDecoder 类
 */
public class C_IntegerToStringDecoder extends MessageToMessageDecoder<Integer> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, Integer integer, List<Object> list) throws Exception {
        //将Integer消息转换成String,并添加到输出的list中
        list.add(String.valueOf(integer));
    }
}
