package com.agrahame.terminal.chat.server;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 Sets up the servers pipeline.
 */
public class NetworkChannelInitializer extends ChannelInitializer<SocketChannel> {

    /** Handles upstream events. */
	private final ChannelInboundHandlerAdapter channelHandler = new NetworkChannelHandler();
	
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelDuplexHandler timeout = new IdleStateHandler(5, 0, 0);
		ch.pipeline().addLast("decoder", new StringDecoder());
		ch.pipeline().addLast("encoder", new StringEncoder());		
		ch.pipeline().addLast("handler", channelHandler);
		ch.pipeline().addLast("timeout", timeout);
	}	

}
