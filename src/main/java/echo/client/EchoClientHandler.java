package echo.client;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {
	
	private final ByteBuf firstMessage;
	
	public EchoClientHandler() {
		firstMessage = Unpooled.buffer(EchoClient.SIZE);
		for (int i=0; i<firstMessage.capacity(); i++) {
			firstMessage.writeByte((byte)i);
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("전송한 문자열 {} " + firstMessage.toString());
		ctx.writeAndFlush(firstMessage);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String readMessage = ((ByteBuf)msg).toString(Charset.defaultCharset());
		System.out.println("수신한 문자열 {} " + readMessage);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	
	
}
