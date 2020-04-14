package nio.chapter11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * 代码清单 11-1 添加 SSL/TLS 支持
 * 在大多数情况下，SslHandler 将是ChannelPipeline 中的第一个ChannelHandler。
 这确保了只有在所有其他的ChannelHandler 将它们的逻辑应用到数据之后，才会进行加密。
 */
public class A_SslChannelInitializer extends ChannelInitializer<Channel> {
    private final SslContext context;
    private final boolean startTls;

    /**
     * 传入要使用的 SslContext
     * @param context
     * @param startTls  如果设置为 true，第一个写入的消息将不会被加密（客户端应该设置为 true）
     */
    public A_SslChannelInitializer(SslContext context, boolean startTls) {
        this.context = context;
        this.startTls = startTls;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        SSLEngine engine = context.newEngine(channel.alloc());
        channel.pipeline().addFirst("ssl", new SslHandler(engine, startTls));
    }
}
