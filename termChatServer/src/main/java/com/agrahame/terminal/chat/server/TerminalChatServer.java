package com.agrahame.terminal.chat.server;
import java.util.logging.Logger;

/**
 * terminal chat server.
 */
public class TerminalChatServer {
	
	public static final Logger logger = Logger.getLogger(TerminalChatServer.class.getName());
	public static final int SERVER_PORT = 7777;


	private TerminalChatServer() {
		throw new UnsupportedOperationException("not allowed");
	}
	
	public static void main(String[] args) throws InterruptedException {	
		new NetworkBuilder().bind(SERVER_PORT);
	}

}
