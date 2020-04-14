package nio.chapter11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 代码清单 11-4 自动压缩 HTTP 消息
 * <p>
 * 服务器没有义务压缩它所发送的数据。
 *
 * @author WangChao
 * @create 2018/3/21 21:23
 */
public class D_HttpCompressionInitializer extends ChannelInitializer<Channel> {
    private final boolean isClient;

    public D_HttpCompressionInitializer(boolean isClient) {
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        if (isClient){
            pipeline.addLast("codec",new HttpClientCodec());
            //如果是客户端,添加HttpContentCompressor以处理来自服务器的压缩内容
            pipeline.addLast("decompressor",new HttpContentCompressor());
        }else{
            pipeline.addLast("codec",new HttpServerCodec());
            //如果是服务器,添加HttpContentCompressor来压缩数据(如果客户端支持)
            pipeline.addLast("decompressor",new HttpContentCompressor());
        }

    }
}

