package nio.chapter5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ByteProcessor;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

import static nio.chapter5.DummyChannelHandlerContext.DUMMY_INSTANCE;

/**
 * ByteBuf 的使用模式
 */
public class ByteBufExamples {
    private static final ByteBuf BYTE_BUF_FROM_SOMEWHERE = Unpooled.buffer(1024);
    private final static Random random = new Random();

    /**
     * 支撑数组
     * 最常用的ByteBuf 模式是将数据存储在JVM 的堆空间中。
     * 这种模式被称为支撑数组（backing array），它能在没有使用池化的情况下提供快速的分配和释放。
     */
    public static void heapBuffer() {
        ByteBuf heapBuf = BYTE_BUF_FROM_SOMEWHERE;
        //检查ByteBuf是否有一个支撑数组
        if (heapBuf.hasArray()) {
            //获取数组
            byte[] array = heapBuf.array();
            //  计算第一个字节的偏移量
            int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
            //获得可读字节
            int length = heapBuf.readableBytes();
            handleArray(array, offset, length);
        }
    }

    /**
     * 访问直接缓冲区的数据
     */
    public static void directBuffer() {
        ByteBuf directBuf = BYTE_BUF_FROM_SOMEWHERE;
        //检查 ByteBuf 是否由数组支撑。如果不是，则这是一个直接缓冲区
        if (!directBuf.hasArray()) {
            int length = directBuf.readableBytes();
            byte[] array = new byte[length];
            directBuf.getBytes(directBuf.readerIndex(), array);
            handleArray(array, 0, length);
        }

    }

    private static void handleArray(byte[] array, int offset, int length) {

    }

    /**
     * 使用 ByteBuffer 的复合缓冲区模式
     * 它为多个ByteBuffer提供一个聚合视图
     * 分配和复制操作,以及对数组的管理,效率低下
     *
     * @param header
     * @param body
     */
    public static void byteBufferComposite(ByteBuffer header, ByteBuffer body) {
        ByteBuffer[] message = {header, body};
        ByteBuffer message2 = ByteBuffer.allocate(header.remaining() + body.remaining());
        message2.put(header);
        message2.put(body);
        message2.flip();
    }

    /**
     * 使用 CompositeByteBuf 的复合缓冲区模式
     * 可能不支持支撑数组
     */
    public static void byteBufComposite() {
        CompositeByteBuf messageBuf = Unpooled.compositeBuffer();
        ByteBuf headerBuf = BYTE_BUF_FROM_SOMEWHERE;
        ByteBuf bodyBuf = BYTE_BUF_FROM_SOMEWHERE;
        //将 ByteBuf 实例追加到 CompositeByteBuf
        messageBuf.addComponents(headerBuf, bodyBuf);
        //删除位于索引位置为 0（第一个组件）的 ByteBuf
        messageBuf.removeComponent(0);
        //循环遍历所有的 ByteBuf 实例
        for (ByteBuf buf : messageBuf) {
            System.out.println(buf.toString());
        }
    }

    /**
     * 代码清单 5-6 访问数据
     * 访问数据
     * 不会改变
     * readerIndex 也不会改变writerIndex。
     * 通过调用readerIndex(index)或者writerIndex(index)来手动移动这两者
     */
    public static void byteBufRelativeAccess() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE;
        for (int i = 0; i < buffer.capacity(); i++) {
            byte b = buffer.getByte(i);
            System.out.println((char) b);
        }
    }

    /**
     * 代码清单 5-7 读取所有数据
     */
    public static void readAllData() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE;
        while (buffer.isReadable()) {
            System.out.println(buffer.readByte());
        }
    }

    /**
     * 代码清单 5-8 写数据
     */
    public static void write() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE;
        while (buffer.writableBytes() >= 4) {
            buffer.writeInt(random.nextInt());
        }
    }

    /**
     * 代码清单 5-9 使用 ByteBufProcessor 来寻找\r
     * <p>
     * use {@link io.netty.buffer.ByteBufProcessor in Netty 4.0.x}
     */
    public static void byteProcessor() {
        ByteBuf buffer = BYTE_BUF_FROM_SOMEWHERE;
        int index = buffer.forEachByte(ByteProcessor.FIND_CR);
    }

    /**
     * 代码清单 5-10 对 ByteBuf 进行切片
     * duplicate/slice/Unpooled.unmodifiableBuffer/order/readSlice:返回新的ByteBuf实例,它具有自己的读索引、写索引和标记索引。其内部存储和JDK 的ByteBuffer 一样也是共享的
     */
    public static void byteBufSlice() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        ByteBuf sliced = buf.slice(0, 15);
        System.out.println(sliced.toString(utf8));
        buf.setByte(0, (byte) 'J');
        //将会成功，因为数据是共享的，对其中一个所做的更改对另外一个也是可见的
        assert buf.getByte(0) == sliced.getByte(0);
    }

    /**
     * 代码清单 5-11 复制一个 ByteBuf
     */
    public static void byteBufCopy() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        ByteBuf copy = buf.copy(0, 15);
        System.out.println(copy.toString(utf8));
        buf.setByte(0, (byte) 'J');
        //将会成功，因为数据不是共享的
        assert buf.getByte(0) != copy.getByte(0);
    }

    /**
     * 代码清单 5-12 get()和 set()方法的用法
     */
    public static void byteBufSetGet() {
        Charset utf8 = Charset.forName("UTF-8");
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        System.out.println((char) buf.getByte(0));
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();
        buf.setByte(0, (byte) 'B');
        System.out.println((char) buf.getByte(0));
        //将会成功，因为这些操作并不会修改相应的索引
        assert readerIndex == buf.readerIndex();
        assert writerIndex == buf.writerIndex();
    }

    /**
     * 代码清单 5-13 ByteBuf 上的 read()和 write()操作
     */
    public static void byteBufWriteRead() {
        Charset utf8 = Charset.forName("UTF-8");
        //创建一个新的 ByteBuf 以保存给定字符串的字节
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        //打印第一个字符'N'
        System.out.println((char) buf.readByte());
        //存储当前的readerIndex
        int readerIndex = buf.readerIndex();
        //存储当前的writerIndex
        int writerIndex = buf.writerIndex();
        //将字符 '?'追加到缓冲区
        buf.writeByte((byte) '?');
        assert readerIndex == buf.readerIndex();
        //将会成功，因为 writeByte()方法移动了 writerIndex
        assert writerIndex != buf.writerIndex();
    }

    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();
    private static final ChannelHandlerContext CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE = DUMMY_INSTANCE;

    /**
     * 代码清单 5-14 获取一个到 ByteBufAllocator 的引用
     * 可以创建基于直接内存,堆的buf实例
     * 通过interface ByteBufAllocator 实现了
     （ByteBuf 的）池化，它可以用来分配我们所描述过的任意类型的ByteBuf 实例

     Netty提供了两种ByteBufAllocator的实现：PooledByteBufAllocator和Unpooled-ByteBufAllocator。
     前者池化了ByteBuf的实例以提高性能并最大限度地减少内存碎片。此实现使用了一种称为jemalloc的已被大量现代操作系统所采用的高效方法来分配内存。
     后者的实现不池化ByteBuf实例，并且在每次它被调用时都会返回一个新的实例。
     Netty默认使用了PooledByteBufAllocator
     */
    public static void obtainingByteBufAllocatorReference() {
        Channel channel = CHANNEL_FROM_SOMEWHERE;
        //从 Channel 获取一个到ByteBufAllocator 的引用
        ByteBufAllocator allocator = channel.alloc();

        ChannelHandlerContext ctx=CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE;
        //从 ChannelHandlerContext 获取一个到 ByteBufAllocator 的引用
        ByteBufAllocator allocator1 = ctx.alloc();
    }

    /**
     * 代码清单 5-15 引用计数
     */
    public static  void referenceCounting(){
        Channel channel=CHANNEL_FROM_SOMEWHERE;
        ByteBufAllocator allocator = channel.alloc();
        ByteBuf buf = allocator.directBuffer();
        //检查引用计数是否为预期的 1
        assert buf.refCnt()==1;
    }

    /**
     * 代码清单 5-16 释放引用计数的对象
     * 谁负责释放 一般来说，是由最后访问（引用计数）对象的那一方来负责将它释放
     */
    public static void releaseReferenceCountedObject(){
        ByteBuf buf=BYTE_BUF_FROM_SOMEWHERE;
        //减少到该对象的活动引用。当减少到 0 时，该对象被释放，并且该方法返回 true
        boolean release = buf.release();
    }
}

