package nio.chapter11;

import io.netty.channel.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedStream;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;
import java.io.FileInputStream;

/**
 * 写大型数据
 * 代码清单 11-12 使用 ChunkedStream 传输文件内容
 *
 * 每个都代表了一个将由Chunked-WriteHandler 处理的不定长度的数据流。
 * ChunkedFile 从文件中逐块获取数据，当你的平台不支持零拷贝或者你需要转换数据时使用
 ChunkedNioFile 和ChunkedFile 类似，只是它使用了FileChannel
 ChunkedStream 从InputStream 中逐块传输内容
 ChunkedNioStream 从ReadableByteChannel 中逐块传输内容

 * @author WangChao
 * @create 2018/3/24 11:35
 */
public class L_ChunkedWriteHandlerInitializer extends ChannelInitializer<Channel> {
    private final File file;
    private final SslContext sslContext;

    public L_ChunkedWriteHandlerInitializer(File file, SslContext sslContext) {
        this.file = file;
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new SslHandler(sslContext.newEngine(channel.alloc())));
        //添加 ChunkedWriteHandler 以处理作为 ChunkedInput 传入的数据
        pipeline.addLast(new ChunkedWriteHandler());
        //一旦连接建立，WriteStreamHandler 就开始写文件数据
        pipeline.addLast(new WriteStreamHandler());
    }

    private final class WriteStreamHandler extends ChannelInboundHandlerAdapter {
        @Override
        //当连接建立时，channelActive() 方法将使用 ChunkedInput 写文件数据
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            ctx.writeAndFlush(new ChunkedStream(new FileInputStream(file)));
        }
    }
}
