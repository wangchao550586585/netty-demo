package nio.chapter11;

import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.File;
import java.io.FileInputStream;

/**
 * 写大型数据
 * 代码清单 11-11 使用 FileRegion 传输文件的内容
 * NIO 的零拷贝特性，这种特性消除了将文件的内容从文件系统移动到网络栈的复制过程
 * FileRegion:“通过支持零拷贝的文件传输的Channel 来发送的文件区域。”
 *
 * 这个示例只适用于文件内容的直接传输，不包括应用程序对数据的任何处理。在需要将数据
 从文件系统复制到用户内存中时，可以使用ChunkedWriteHandler，它支持异步写大型数据
 流，而又不会导致大量的内存消耗。
 * @author WangChao
 * @create 2018/3/24 11:24
 */
public class K_FileRegionWriteHandler extends ChannelInboundHandlerAdapter {
    private static final Channel CHANNEL_FROM_SOMEWHERE=new NioSocketChannel();
    private static final File FILE_FROM_SOMEWHERE=new File("");

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        File file=FILE_FROM_SOMEWHERE;
        Channel channel=CHANNEL_FROM_SOMEWHERE;
        FileInputStream in = new FileInputStream(file);
        //以该文件的完整长度创建一个新的DefaultFileRegion
        DefaultFileRegion region = new DefaultFileRegion(in.getChannel(), 0, file.length());
        channel.writeAndFlush(region).addListener((ChannelFutureListener) channelFuture -> {
            if (!channelFuture.isSuccess()){
                //处理失败
                Throwable cause = channelFuture.cause();
            }
        });
    }
}
