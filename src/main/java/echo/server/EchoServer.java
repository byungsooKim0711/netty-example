package echo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EchoServer {

	private static final int PORT_NUMBER = 12345;

	public static void main(String... args) throws Exception {
		new EchoServer().start();
	}
	
	public void start() throws Exception {
		EventLoopGroup parentGroup = new NioEventLoopGroup(1);
		EventLoopGroup childGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(parentGroup, childGroup)
			 .channel(NioServerSocketChannel.class)
			 .option(ChannelOption.SO_BACKLOG, 100)
			 .handler(new LoggingHandler(LogLevel.INFO))
			 .childHandler(new EchoServerChannelInitializer());

			ChannelFuture f = b.bind(PORT_NUMBER).sync();

			f.channel().closeFuture().sync();
		} finally {
			parentGroup.shutdownGracefully();
			childGroup.shutdownGracefully();
		}
	}
}
