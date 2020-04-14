package nio.chapter12;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * 代码清单 12-2 处理文本帧
 *
 * TextWebSocketFrameHandler 只有一组非常少量的责任。当和新客户端的WebSocket
 握手成功完成之后，它将通过把通知消息写到ChannelGroup 中的所有Channel 来通知所
 有已经连接的客户端，然后它将把这个新Channel 加入到该ChannelGroup 中。
 如果接收到了TextWebSocketFrame 消息，TextWebSocketFrameHandler 将调用
 TextWebSocketFrame 消息上的retain()方法，并使用writeAndFlush()方法来将它传
 输给ChannelGroup，以便所有已经连接的WebSocket Channel 都将接收到它。
 和之前一样，对于retain()方法的调用是必需的，因为当channelRead0()方法返回时，
 TextWebSocketFrame 的引用计数将会被减少。由于所有的操作都是异步的，因此，writeAnd-
 Flush()方法可能会在channelRead0()方法返回之后完成，而且它绝对不能访问一个已经失
 效的引用

 * @author WangChao
 * @create 2018/3/28 18:51
 */
//扩展 SimpleChannelInboundHandler，并处理 TextWebSocketFrame 消息
//    其还将在它的ChannelGroup 中跟踪所有活动的WebSocket 连接。
public class B_TextWebSocketFrameHandler extends  SimpleChannelInboundHandler<TextWebSocketFrame> {
    private final ChannelGroup group;

    public B_TextWebSocketFrameHandler(ChannelGroup group) {
        this.group = group;
    }

    //重写 userEventTriggered()方法以处理自定义事件
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx,
                                   Object evt) throws Exception {
        //如果该事件表示握手成功，则从该 ChannelPipeline 中移除HttpRequest-Handler，因为将不会接收到任何HTTP消息了
        if (evt == WebSocketServerProtocolHandler
                .ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            ctx.pipeline().remove(A_HttpRequestHandler.class);
            //(1) 通知所有已经连接的 WebSocket 客户端新的客户端已经连接上了
            group.writeAndFlush(new TextWebSocketFrame(
                    "Client " + ctx.channel() + " joined"));
            //(2) 将新的 WebSocket Channel 添加到 ChannelGroup 中，以便它可以接收到所有的消息
            group.add(ctx.channel());
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx,
                             TextWebSocketFrame msg) throws Exception {
        //(3) 增加消息的引用计数，并将它写到 ChannelGroup 中所有已经连接的客户端
        group.writeAndFlush(msg.retain());
    }
}
