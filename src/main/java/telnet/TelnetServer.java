package telnet;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class TelnetServer {

	private static final int PORT_NUMBER = 8023;
	
	public static void main(String[] args) throws Exception {
		new TelnetServer().start();
	}
	
	private void start() throws Exception {
		EventLoopGroup parentGroup = new NioEventLoopGroup(1);
		EventLoopGroup childGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(parentGroup, childGroup)
			 .channel(NioServerSocketChannel.class)
			 .handler(new LoggingHandler(LogLevel.INFO))
			 .childHandler(new TelnetServerInitializer());
			
			b.bind(PORT_NUMBER).sync().channel().closeFuture().sync();
		} finally {
			parentGroup.shutdownGracefully();
			childGroup.shutdownGracefully();
		}
	}
}
