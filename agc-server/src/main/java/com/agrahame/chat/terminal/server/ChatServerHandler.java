package com.agrahame.chat.terminal.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.netty.channel.Channel;

public class ChatServerHandler extends ChannelInboundMessageHandlerAdapter<String> {

    private static final ChannelGroup channels = new DefaultChannelGroup();

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            System.out.println("\n\n[SERVER] -  " + incoming.remoteAddress() + "  has joined");
        }
        channels.add(ctx.channel());
        showChatRoomSize();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            System.out.println("\n\n[SERVER] -  " + incoming.remoteAddress() + "  has left");
        }
        channels.remove(ctx.channel());
        showChatRoomSize();
    }
    
    @Override
    public void messageReceived(ChannelHandlerContext channelHandlerContext, String chatMsg) throws Exception {
    	//DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss"); 
    	DateFormat df = new SimpleDateFormat("EEE HH:mm:ss"); 
    	Date dateobj = new Date(); 
    	final String timestamp = df.format(dateobj);
        Channel incoming = channelHandlerContext.channel();
        for (Channel channel : channels) {
            if (channel != incoming){
                //channel.write("[" + incoming.remoteAddress() + "]" + s + "\n");
                channel.write(timestamp + " " + chatMsg + "\n");
            }
        }
    }

    private void showChatRoomSize() {
        System.out.println("[SERVER] - num people in chat room : " + channels.size());
    }

}
