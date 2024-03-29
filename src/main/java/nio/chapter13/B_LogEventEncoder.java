package nio.chapter13;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 代码清单 13-2 LogEventEncoder
 *
 * @author WangChao
 * @create 2018/4/13 23:46
 */
public class B_LogEventEncoder extends MessageToMessageEncoder<A_LogEvent> {
    private final InetSocketAddress remoteAddress;
    //LogEventEncoder 创建了即将被发送到指定的 InetSocketAddress 的 DatagramPacket 消息
    public B_LogEventEncoder(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext,
                          A_LogEvent logEvent, List<Object> out) throws Exception {
        byte[] file = logEvent.getLogfile().getBytes(CharsetUtil.UTF_8);
        byte[] msg = logEvent.getMsg().getBytes(CharsetUtil.UTF_8);
        ByteBuf buf = channelHandlerContext.alloc()
                .buffer(file.length + msg.length + 1);
        //将文件名写入到 ByteBuf 中
        buf.writeBytes(file);
        //添加一个 SEPARATOR
        buf.writeByte(A_LogEvent.SEPARATOR);
        //将日志消息写入 ByteBuf 中
        buf.writeBytes(msg);
        //将一个拥有数据和目的地地址的新 DatagramPacket 添加到出站的消息列表中
        out.add(new DatagramPacket(buf, remoteAddress));
    }
}
