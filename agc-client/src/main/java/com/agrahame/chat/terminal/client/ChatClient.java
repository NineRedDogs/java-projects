package com.agrahame.chat.terminal.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;


public class ChatClient {
	
    public static void main(String[] args) throws Exception{
        //new ChatClient("localhost", 6666).run();
        new ChatClient("35.176.228.220", 6666).run();
    }

    //private String username = System.getProperty("chat.user");
    private final String host;
    private final int port;

    public ChatClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception{
        String username = System.getProperty("chat.user");
    	System.out.println("username [" + username + "]");
    	while ((username == null || username.isEmpty())) {
    		// no user provided prompt for it before we get started ....
    		
    		// create a scanner so we can read the command-line input
    	    Scanner scanner = new Scanner(System.in);

    	    //  prompt for the user's name
    	    System.out.print("Enter your name: ");

    	    // get their input as a String
    	    username = scanner.next();
    	}
    	
    	System.out.println("\nHi " + username + ", we are about to connect to the chat server, please wait for other people to start yapping ....\n\n");
    	final String msgPrefix = "[" + username + "] ";
    	
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap  = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChatClientInit());
            Channel channel = bootstrap.connect(host, port).sync().channel();
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            channel.write(msgPrefix + "Hi, this is " + username + "\r\n");
            String myMessage="";
            boolean left = false;
            
            while(!left){
            	myMessage = in.readLine();
                channel.write(msgPrefix + myMessage + "\r\n");
                left = myMessage.equalsIgnoreCase("bye");
            }
            System.out.println("Bye " + username + ", thanks for stopping by ... ");

            // inform the other room members ....
            channel.write(username + " has left the room ... \r\n");
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }

}
