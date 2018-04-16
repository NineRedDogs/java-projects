package com.agrahame.terminal.chat.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientAdapterHandler extends
SimpleChannelInboundHandler<String> {

	@Override
	public void channelRead(ChannelHandlerContext arg0, Object arg1)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void channelReadComplete(ChannelHandlerContext arg0)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext arg0)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		Channel currentChannel = ctx.channel();
		System.out.println("[INFO-client] - " + currentChannel.remoteAddress() + " - " + msg);
		currentChannel.write("[Server] - Success");		
	}

}
