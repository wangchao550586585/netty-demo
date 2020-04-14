package nio.chapter7;

import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleExamples {
    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();

    /**
     * 代码清单 7-2 使用 ScheduledExecutorService 调度任务
     * 缺点:作为线程池管理的一部分，将会有额外的线程创建
     */
    public static void schedule() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("Now it is 60 seconds later");
            }
        }, 60, TimeUnit.SECONDS);
        executor.shutdown();
    }

    /**
     * 代码清单 7-3 使用 EventLoop 调度任务
     */
    public static void scheduleViaEventLoop() {
        Channel ch = CHANNEL_FROM_SOMEWHERE;
        ScheduledFuture<?> schedule = ch.eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                //要执行的代码
                System.out.println("60 seconds later");
            }
            //调度任务在从现在开始的 60 秒之后执行
        }, 60, TimeUnit.SECONDS);
    }
    /**
     * 代码清单 7-4 使用 EventLoop 调度周期性的任务
     * */
    public static void scheduleFixedViaEventLoop() {
        Channel ch = CHANNEL_FROM_SOMEWHERE;
        ch.eventLoop().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //这将一直运行，直到 ScheduledFuture 被取消
                System.out.println("Run every 60 seconds");
            }
            //调度在 60 秒之后，并且以后每间隔 60 秒运行
        }, 60, 60, TimeUnit.SECONDS);
    }

    /**
     * 代码清单 7-5 使用 ScheduledFuture 取消任务
     * */
    public  static void cancelingTaskUsingScheduledFuture(){
        Channel ch=CHANNEL_FROM_SOMEWHERE;
        ScheduledFuture<?> future = ch.eventLoop().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //这将一直运行，直到 ScheduledFuture 被取消
                System.out.println("Run every 60 seconds");
            }
            //调度在 60 秒之后，并且以后每间隔 60 秒运行
        }, 60, 60, TimeUnit.SECONDS);
        boolean mayInterruptIfRunning = false;
        //取消该任务，防止它再次运行
        future.cancel(mayInterruptIfRunning);
    }












}
