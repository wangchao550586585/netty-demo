package nio.chapter11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * 代码清单 11-5 使用 HTTPS
 *
 * @author WangChao
 * @create 2018/3/24 10:00
 */
public class E_HttpsCodecInitializer extends ChannelInitializer<Channel> {
    private final SslContext sslContext;
    private final boolean isClient;

    public E_HttpsCodecInitializer(SslContext sslContext, boolean isClient) {
        this.sslContext = sslContext;
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        SSLEngine engine = sslContext.newEngine(channel.alloc());
        pipeline.addFirst("ssl", new SslHandler(engine));
        if (isClient){
            pipeline.addLast("codec",new HttpClientCodec());
        }else{
            pipeline.addLast("codec",new HttpServerCodec());
        }
    }
}
