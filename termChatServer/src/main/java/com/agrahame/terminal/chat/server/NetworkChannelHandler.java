package com.agrahame.terminal.chat.server;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Handles upstream messages.
 */
public class NetworkChannelHandler extends SimpleChannelInboundHandler<String> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		System.out.println("[" + ctx.channel().remoteAddress() + "] Message - " + msg);
	}
	
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
    	e.printStackTrace();
    	ctx.channel().close();
    }

}
