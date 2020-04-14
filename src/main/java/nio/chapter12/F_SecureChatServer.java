package nio.chapter12;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.SSLException;
import java.net.InetSocketAddress;
import java.security.cert.CertificateException;

/**
 * 代码清单 12-7 向 ChatServer 添加加密
 */
//SecureChatServer 扩展 ChatServer 以支持加密
public class F_SecureChatServer extends D_ChatServer {
    private final SslContext context;

    public F_SecureChatServer(SslContext context) {
        this.context = context;
    }

    @Override
    protected ChannelHandler createInitializer(ChannelGroup channelGroup) {
        return new E_SecureChatServerInitializer(channelGroup, context);
    }

    public static void main(String[] args) throws Exception {
        /*if (args.length != 1) {
            System.err.println("Please give port as argument");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);*/
        int port = 9999;
        SelfSignedCertificate cert = new SelfSignedCertificate();
        SslContext context = SslContext.newServerContext(cert.certificate(), cert.privateKey());
        final F_SecureChatServer endpoint = new F_SecureChatServer(context);
        final ChannelFuture future = endpoint.start(new InetSocketAddress(port));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            endpoint.destory();
        }));
        future.channel().closeFuture().syncUninterruptibly();

    }
}
