package com.agrahame.terminal.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Terminal Chat Client.
 */
public class TerminalChatClient {

	/**
	 * The address that the client will connect to.
	 */
	public static final String SERVER_ADDRESS = "localhost";
	
	/**
	 * The address that the client will connect to.
	 */
	public static final int SERVER_PORT = 7777;


	public static void main(String[] args) throws InterruptedException {
		connect(SERVER_ADDRESS, SERVER_PORT);
	}

	/**
	 * Connects the client to the server.
	 * 
	 * @param address address of server
	 * @param port port of server
	 * @throws InterruptedException
	 */
	public static void connect(String address, int port) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
			.channel(NioSocketChannel.class)
			.handler(new NetworkChannelInitializer());

			Channel ch = b.connect(address, port).sync().channel();
			System.out.println("The client is now connected to server: " + address + ":" + port);

			ChannelFuture lastWriteFuture = null;

			// The reader that will read the users input.
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

			// Declaring the message
			String message = "";

			try {
				while (!message.contains("exit")) {
					message = reader.readLine();
					if (message == null) {
						continue;
					}

					lastWriteFuture = ch.writeAndFlush(message);

					if (message.equalsIgnoreCase("bye")) {
						System.out.println("\n\n...and its goodnight from him ...\n\n");
					}

				}
			} catch (IOException ex) {
				System.out.println("An error occured while trying to write a message." + ex);
			}

			if (lastWriteFuture != null) {
				lastWriteFuture.sync();
			}

		} finally {
			group.shutdownGracefully();
		}
	}

}
