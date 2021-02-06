package com.xizi.inboundhandlerAndoutboundhandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {

    //客户端读取服务器发送的信息
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("------------------------");
        System.out.println("服务器的ip="+ctx.channel().remoteAddress());
        System.out.println("收到服务器发送的消息="+msg);
    }

    //重写channelActive发送数据给服务端
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler 发送数据");
        ctx.writeAndFlush(123465L);

        //注意传入的数据类型和处理的数据类型要一致
//        ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcd", CharsetUtil.UTF_8));
    }
}
