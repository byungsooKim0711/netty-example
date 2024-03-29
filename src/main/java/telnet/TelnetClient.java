package telnet;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TelnetClient {

	private static final String HOST = "127.0.0.1";
	private static final int PORT_NUMBER = 8023;

	public static void main(String[] args) throws Exception {
		new TelnetClient().start();
	}

	private void start() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).handler(new TelnetClientInitializer());

			Channel ch = b.connect(HOST, PORT_NUMBER).sync().channel();

			ChannelFuture lastWriteFuture = null;
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

			while (true) {
				String line = in.readLine();
				if (line == null) {
					break;
				}

				lastWriteFuture = ch.writeAndFlush(line + "\r\n");

				if ("bye".equals(line.toLowerCase())) {
					ch.closeFuture().sync();
					break;
				}
			}

			if (lastWriteFuture != null) {
				lastWriteFuture.sync();
			}

		} finally {
			group.shutdownGracefully();
		}
	}
}
