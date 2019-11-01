package echo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {

	static final int PORT_NUMBER = 12345;
	static final int SIZE = 256;
	static final String HOST = "127.0.0.1";

	public static void main(String... args) throws Exception {
		new EchoClient().start();
	}
	
	public void start() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
			 .channel(NioSocketChannel.class)
			 .option(ChannelOption.TCP_NODELAY, true)
			 .handler(new EchoClientChannelInitializer());

			ChannelFuture f = b.connect(HOST, PORT_NUMBER).sync();
			
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}
}