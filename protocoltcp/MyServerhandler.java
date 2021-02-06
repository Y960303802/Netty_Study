package com.xizi.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

//处理业务的handler
public class MyServerhandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {

        int len = msg.getLen();
        byte[] content = msg.getContent();
        //将buffer转为字符串
        String message = new String(content, Charset.forName("utf-8"));
        System.out.println("服务器收到的数据="+message);
        System.out.println("服务器收到的数据长度="+len);
        System.out.println("服务器收到的消息包数量="+(++this.count));

        //服务器回送数据给客户端 回送一个随机id
        String responseContent = UUID.randomUUID().toString();
        int responeseLen=responseContent.getBytes("utf-8").length;
        //构建一个协议包
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(responeseLen);
        messageProtocol.setContent(responseContent.getBytes("utf-8"));
        ctx.writeAndFlush(messageProtocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
