package echo.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class EchoClientChannelInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new EchoClientHandler());
	}
}