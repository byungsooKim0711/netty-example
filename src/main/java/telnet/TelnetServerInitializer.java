package telnet;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class TelnetServerInitializer extends ChannelInitializer<SocketChannel> {

	private static final StringDecoder DECODER = new StringDecoder();
	private static final StringEncoder ENCODER = new StringEncoder();
	
	private static final TelnetServerHandler SERVER_HANDLER = new TelnetServerHandler();
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new DelimiterBasedFrameDecoder(102400, Delimiters.lineDelimiter()), DECODER, ENCODER,SERVER_HANDLER);
	}
}
