package nio.chapter6;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelPipeline;

/**
 * 代码清单 6-5 修改 ChannelPipeline
 */
public class E_ModifyChannelPipeline {
    private static final ChannelPipeline CHANNEL_PIPELINE_FROM_SOMEWHERE = DummyChannelPipeline.DUMMY_INSTANCE;

    public static void modifyPipeline() {
        ChannelPipeline pipeline = CHANNEL_PIPELINE_FROM_SOMEWHERE;
        FirstHandler firstHandler = new FirstHandler();
        //将该实例作为"handler1"添加到ChannelPipeline 中
        pipeline.addLast("handler1", firstHandler);
        //添加到 ChannelPipeline的第一个槽中。这意味着它将被放置在已有的"handler1"之前
        pipeline.addFirst("handler2",  new SecondHandler());
        //将一个 third 的实例作为"handler3"添加到 ChannelPipeline 的最后一个槽中
        pipeline.addLast("handler3", new ThirdHandler());

        //通过名称移除"handler3"
        pipeline.remove("handler3");
        //通过引用移除FirstHandler（它是唯一的，所以不需要它的名称）
        pipeline.remove(firstHandler);
        //将 SecondHandler("handler2")替换为 FourthHandler:"handler4"
        pipeline.replace("handler2", "handler4", new FourthHandler());

        //通过类型或者名称返回ChannelHandler
        pipeline.get("handler3");
        //返回和ChannelHandler 绑定的ChannelHandlerContext
        pipeline.context("handler3");
        //返回ChannelPipeline 中所有ChannelHandler 的名称
        pipeline.names();
    }

    private static final class FirstHandler
            extends ChannelHandlerAdapter {

    }

    private static final class SecondHandler
            extends ChannelHandlerAdapter {

    }

    private static final class ThirdHandler
            extends ChannelHandlerAdapter {

    }

    private static final class FourthHandler
            extends ChannelHandlerAdapter {

    }
}
