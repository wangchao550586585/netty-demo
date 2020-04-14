package nio.chapter4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChannelOperationExamples {
    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();

    /**
     * 写出到Channel
     */
    public static void writingToChannel() {
        Channel channel = CHANNEL_FROM_SOMEWHERE;
        ByteBuf buf = Unpooled.copiedBuffer("your data", CharsetUtil.UTF_8);
        ChannelFuture cf = channel.writeAndFlush(buf);
        //添加 ChannelFutureListener 以便在写操作完成后接收通知
        cf.addListener((ChannelFutureListener) future -> {
            //写操作完成，并且没有错误发生
            if (future.isSuccess()) {
                System.out.println("Write successful");
            } else {
                //记录错误
                System.err.println("Write error");
                future.cause().printStackTrace();
            }
        });
    }

    /**
     * 从多个线程使用同一个 Channel
     * Netty 的Channel 实现是线程安全的
     * 消息将会被保证按顺序发送
     */
    public static void writingToChannelFromManyThreads() {
        final Channel channel = CHANNEL_FROM_SOMEWHERE;
        final ByteBuf buf = Unpooled.copiedBuffer("your data", CharsetUtil.UTF_8);
        Runnable write = () -> channel.write(buf.duplicate());
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(write);
        executorService.execute(write);
    }
}
