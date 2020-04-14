package nio.chapter6;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 *代码清单 6-11 @Sharable 的错误用法
 * 这段代码的问题在于它拥有状态
 ，即用于跟踪方法调用次数的实例变量count。将这个类
 的一个实例添加到ChannelPipeline将极有可能在它被多个并发的Channel访问时导致问
 题。（当然，这个简单的问题可以通过使channelRead()方法变为同步方法来修正。,也可以通过使用AtomicInteger来规避这个问题）
 总之，只应该在确定了你的ChannelHandler 是线程安全的时才使用@Sharable 注解。
 */
//使用注解@Sharable标注
@ChannelHandler.Sharable
public class H_UnsharableHandler extends ChannelInboundHandlerAdapter {
    private int count;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        count++;
        //记录方法调用，并转发给下一个ChannelHandler
        System.out.println("inboundBufferUpdated(...) called the "
                + count + " time");
        ctx.fireChannelRead(msg);
    }
}
