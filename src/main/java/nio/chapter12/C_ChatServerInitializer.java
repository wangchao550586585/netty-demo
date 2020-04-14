package nio.chapter12;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * 代码清单 12-3 初始化 ChannelPipeline
 *
 * 当WebSocket 协议升级完成之后，WebSocketServerProtocolHandler 将会把Http-
 RequestDecoder 替换为WebSocketFrameDecoder，把HttpResponseEncoder 替换为
 WebSocketFrameEncoder。为了性能最大化，它将移除任何不再被WebSocket 连接所需要的
 ChannelHandler
 * @author WangChao
 * @create 2018/4/13 14:58
 */
public class C_ChatServerInitializer extends ChannelInitializer<Channel> {
    private final ChannelGroup group;

    public C_ChatServerInitializer(ChannelGroup group) {
        this.group = group;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
/*        将字节解码为HttpRequest、HttpContent 和LastHttp-
                Content。并将HttpRequest、HttpContent 和Last-
                HttpContent 编码为字节
                */
        pipeline.addLast(new HttpServerCodec());

        /*在需要将数据
        从文件系统复制到用户内存中时，可以使用ChunkedWriteHandler，它支持异步写大型数据
        流，而又不会导致大量的内存消耗*/
        pipeline.addLast(new ChunkedWriteHandler());

        /*将一个HttpMessage 和跟随它的多个HttpContent 聚合
        为单个FullHttpRequest 或者FullHttpResponse（取
        决于它是被用来处理请求还是响应）。安装了这个之后，
        ChannelPipeline 中的下一个ChannelHandler 将只会
        收到完整的HTTP 请求或响应*/
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));

        //处理FullHttpRequest（那些不发送到/ws URI 的请求）
        pipeline.addLast(new A_HttpRequestHandler("/ws"));

        /*按照WebSocket 规范的要求，处理WebSocket 升级握手、
        PingWebSocketFrame 、PongWebSocketFrame 和
        CloseWebSocketFrame*/
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        /*处理TextWebSocketFrame 和握手完成事件*/
        pipeline.addLast(new B_TextWebSocketFrameHandler(group));
    }
}
