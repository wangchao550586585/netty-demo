package nio.chapter11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 代码清单 11-3 自动聚合 HTTP 的消息片段
 *
 * 在ChannelInitializer 将ChannelHandler 安装到ChannelPipeline 中之后，你
 便可以处理不同类型的HttpObject 消息了。但是由于HTTP 的请求和响应可能由许多部分组
 成，因此你需要聚合它们以形成完整的消息。为了消除这项繁琐的任务，Netty 提供了一个聚合
 器，它可以将多个消息部分合并为FullHttpRequest 或者FullHttpResponse 消息。通过
 这样的方式，你将总是看到完整的消息内容。
 * @author WangChao
 * @create 2018/3/21 20:19
 */
public class C_HttpAggregatorInitializer extends ChannelInitializer<Channel> {
    private final boolean isClient;

    public C_HttpAggregatorInitializer(boolean isClient) {
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        if (isClient) {
            //如果是客户端,则添加HttpClientCodec
            pipeline.addLast("codec", new HttpClientCodec());
        } else {
            //如果是服务器,则添加HttpServerCodec
            pipeline.addLast("codec", new HttpServerCodec());
        }
        //将最大的消息大小为512KB的HttpObjectAggregator添加到pipeline
        pipeline.addLast("aggregator", new HttpObjectAggregator(512 * 1024));
    }
}
