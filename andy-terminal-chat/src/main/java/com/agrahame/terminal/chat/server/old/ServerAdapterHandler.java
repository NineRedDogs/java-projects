package com.agrahame.terminal.chat.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ServerAdapterHandler extends
SimpleChannelInboundHandler<String> {

    private static final ChannelGroup channels = new DefaultChannelGroup(
            "containers", GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[START] New Container has been initialzed");
        channels.add(ctx.channel());
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[END] A Container has been removed");
        channels.remove(ctx.channel());
        super.handlerRemoved(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext arg0, Object arg1)
            throws Exception {
        System.out.println("channelRead");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext arg0)
            throws Exception {
        // TODO Auto-generated method stub
        System.out.println("channelReadComplete");
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext arg0)
            throws Exception {
        // TODO Auto-generated method stub
        System.out.println("channelWritabilityChanged");
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel currentChannel = ctx.channel();
        System.out.println("[INFO] - " + currentChannel.remoteAddress() + " - " + msg);
        currentChannel.write("[Server] - Success");
    }


}
