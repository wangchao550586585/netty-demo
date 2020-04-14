package nio.chapter10;

import io.netty.channel.CombinedChannelDuplexHandler;
/**
 * 代码清单 CombinedChannelDuplexHandler<I,O>
 * 通过该解码器和编码器实现参数化 CombinedByteCharCodec
 *
 * 这种方式既能够避免这种惩罚，又不会牺牲将一个解码器和一个编码器作为一个单独的单元部署所带来的便利性
 */
public class J_CombinedByteCharCodec extends CombinedChannelDuplexHandler<H_ByteToCharDecoder,I_CharToByteEncoder> {
    public J_CombinedByteCharCodec() {
        //将委托实例传递给父类
        super(new H_ByteToCharDecoder(),new I_CharToByteEncoder());
    }
}
