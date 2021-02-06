package com.xizi.inboundhandlerAndoutboundhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyServerhandler extends SimpleChannelInboundHandler<Long> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("MyServerhandler 读取数据");
        System.out.println("从客户端"+ctx.channel().remoteAddress()+"读取到: "+msg);

        //给客户端发送一个Long
        ctx.writeAndFlush(789456L);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
