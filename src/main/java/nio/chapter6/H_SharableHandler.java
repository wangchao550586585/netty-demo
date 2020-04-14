package nio.chapter6;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 代码清单 6-10 可共享的 ChannelHandler
 *
 * 因为一个ChannelHandler 可以从属于多个ChannelPipeline，所以它也可以绑定到多
 个ChannelHandlerContext 实例。对于这种用法指在多个ChannelPipeline 中共享同一
 个ChannelHandler，对应的ChannelHandler 必须要使用@Sharable 注解标注；

 ChannelHandler 实现符合所有的将其加入到多个ChannelPipeline 的需求，
 即它使用了注解@Sharable 标注，并且也不持有任何的状态
 */
//使用注解@Sharable标注
@ChannelHandler.Sharable
public class H_SharableHandler extends ChannelInboundHandlerAdapter {
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("channel read message " + msg);
        ctx.fireChannelRead(msg);
    }
}
