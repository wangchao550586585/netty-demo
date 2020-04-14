package nio.chapter11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;

/**
 * 代码清单 11-2 添加 HTTP 支持
 */
public class B_HttpPipelineInitializer extends ChannelInitializer<Channel> {
    private final boolean client;

    public B_HttpPipelineInitializer(boolean client) {
        this.client = client;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        if (client) {
            //如果是客户端，则添加 HttpResponseDecoder 以处理来自服务器的响应
            //将字节解码为HttpResponse、HttpContent 和LastHttpContent 消息
            pipeline.addLast("decoder", new HttpResponseDecoder());
            //如果是客户端，则添加 HttpRequestEncoder 以向服务器发送请求
            //将HttpRequest、HttpContent 和LastHttpContent 消息编码为字节
            pipeline.addLast("encoder", new HttpRequestEncoder());
        } else {
            //如果是服务器，则添加 HttpRequestDecoder 以接收来自客户端的请求
            //将字节解码为HttpRequest、HttpContent 和LastHttpContent 消息
            pipeline.addLast("decoder", new HttpRequestDecoder());
            //如果是服务器，则添加 HttpResponseEncoder 以向客户端发送响应
            //将HttpResponse、HttpContent 和LastHttpContent 消息编码为字节
            pipeline.addLast("encoder", new HttpResponseEncoder());
        }
    }
}
