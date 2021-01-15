package com.agrahame.terminal.chat.server;

import java.util.logging.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetector.Level;

/**
 * Sets up system.
 * 
 */
public class NetworkBuilder {
	
	public static final Logger logger = Logger.getLogger(NetworkBuilder.class.getName());
		
	/**
	 * The event loop group that will be attached to bootstrap.
	 */
	private final EventLoopGroup bossGroup = new NioEventLoopGroup();
	
	/**
	 * The event loop group that will be attached to bootstrap.
	 */
	private final EventLoopGroup workerGroup = new NioEventLoopGroup();
	
	/**
	 * Binds the {@link Channel} created by the {@link ServerBootstrap}
	 * to a specified port.
	 * 
	 * @param port
	 * 		The port for the {@link Channel} to bind to.
	 * @throws InterruptedException 
	 */
	public void bind(int port) throws InterruptedException {
		ResourceLeakDetector.setLevel(Level.PARANOID);
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup)
		.channel(NioServerSocketChannel.class)
		.childHandler(new NetworkChannelInitializer())
		.bind(port);		
		System.out.println("Server bound to port: " + port);
	}

}
