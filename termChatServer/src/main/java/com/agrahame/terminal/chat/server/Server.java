package com.core;
import java.util.logging.Logger;

import com.core.net.NetworkBuilder;

/**
 * A simple chat server application using netty 4 api.
 * 
 * @author Chad Adams <https://github.com/Adams94>
 */
public class Server {
	
	/**
	 * The single logger for this class.
	 */
	public static final Logger logger = Logger.getLogger(Server.class.getName());

	/**
	 * The default private constructor.
	 */
	private Server() {
		throw new UnsupportedOperationException("This class cannot be instanciated.");
	}
	
	/**
	 * The main-entry way into the program.
	 * 
	 * @param args
	 *		The command-line arguments.
	 */
	public static void main(String[] args) throws InterruptedException {	
		new NetworkBuilder().bind(Configuration.PORT);
	}

}
