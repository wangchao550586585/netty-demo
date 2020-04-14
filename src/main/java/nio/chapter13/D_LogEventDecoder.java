package nio.chapter13;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.DatagramPacketDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**代码清单 13-6 LogEventDecoder
 * @author WangChao
 * @create 2018/4/14 14:00
 */
public class D_LogEventDecoder extends MessageToMessageDecoder<DatagramPacket> {
    @Override
    protected void decode(ChannelHandlerContext ctx,
                          DatagramPacket datagramPacket, List<Object> out)
            throws Exception {
        //获取对 DatagramPacket 中的数据（ByteBuf）的引用
        ByteBuf data = datagramPacket.content();
        //获取该 SEPARATOR 的索引
        int idx = data.indexOf(0, data.readableBytes(),
                A_LogEvent.SEPARATOR);
        //提取文件名
        String filename = data.slice(0, idx)
                .toString(CharsetUtil.UTF_8);
        //提取日志消息
        String logMsg = data.slice(idx + 1,
                data.readableBytes()).toString(CharsetUtil.UTF_8);
        //构建一个新的 LogEvent 对象，并且将它添加到（已经解码的消息的）列表中
        A_LogEvent event = new A_LogEvent(datagramPacket.sender(),
                System.currentTimeMillis(), filename, logMsg);
        out.add(event);
    }
}
