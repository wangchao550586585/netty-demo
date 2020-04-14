package nio.chapter9;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 代码清单9-2 测试 FixedLengthFrameDecoder
 *
 * @author WangChao
 * @create 2018/3/19 8:19
 */
public class A_FixedLengthFrameDecoderTest {
    @Test
    public void testFramesDecoded() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        //将以3字节的帧长度被测试
        EmbeddedChannel channel = new EmbeddedChannel(new A_FixedLengthFrameDecoder(3));
        //写入数据,同时计数+1
        assertTrue(channel.writeInbound(input.retain()));
        //标记channel已完成,将EmbeddedChannel 标记为完成，
        // 并且如果有可被读取的入站数据或者
        //出站数据，则返回true。这个方法还将会调用EmbeddedChannel 上的close()方法
        assertTrue(channel.finish());

        //读取所生成的消息,并且验证是否有3帧(切片),其中每帧(切片)都为3字节
        ByteBuf read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        //释放引用次数
        read.release();

        read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buf.readSlice(3), read);
        read.release();

        assertNull(channel.readInbound());
        buf.release();
    }

    @Test
    public void testFramesDecoded2() {
        ByteBuf buffer = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buffer.writeByte(i);
        }
        ByteBuf input = buffer.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new A_FixedLengthFrameDecoder(3));
        //返回false,因为没有一个完整的可供读取的帧,至少要3个
        assertFalse(channel.writeInbound(input.readBytes(2)));
        assertTrue(channel.writeInbound(input.readBytes(7)));

        assertTrue(channel.finish());
        ByteBuf read = channel.readInbound();
        assertEquals(buffer.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buffer.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buffer.readSlice(3), read);
        read.release();

        assertNull(channel.readInbound());
        buffer.release();
    }
}
