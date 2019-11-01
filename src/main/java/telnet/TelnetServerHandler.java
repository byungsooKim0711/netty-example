package telnet;

import java.net.InetAddress;
import java.time.ZonedDateTime;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

@Sharable
public class TelnetServerHandler extends SimpleChannelInboundHandler<String> {

	private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.write("Welcom to " + InetAddress.getLocalHost().getHostName() + "-" + InetAddress.getLocalHost().getHostAddress() + "!\r\n");
		ctx.write("Á¢¼Ó½Ã°£ : " + ZonedDateTime.now() + " now.\r\n");
		ctx.flush();
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		channelGroup.stream().forEach(channel -> {
			channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " ´Ô ÀÔÀå.\r\n");
		});
		channelGroup.add(incoming);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		Channel incoming = ctx.channel();
		channelGroup.stream().forEach(channel -> {
			channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + "´Ô ÅðÀå.\r\n");
		});
		channelGroup.remove(incoming);
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

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		Channel incoming = ctx.channel();
		channelGroup.stream().forEach(channel -> {
			if (channel != incoming) {
				channel.writeAndFlush("[" + incoming.remoteAddress() + "]: " + msg + "\r\n");
			}
		});
		
		if ("bye".equals(msg.toLowerCase())) {
			ctx.close();
		}
	}
}