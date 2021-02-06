package com.xizi.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MyTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //服务器收到客户端的消息
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器端收到消息 "+msg.text());

        //回复浏览器消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器发送消息时间: ["+ LocalDateTime.now()+"]\n消息内容："+msg.text()));
    }

    //当web客户端连接后  触发方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //id 表示唯一的值 LongText 是唯一的  ShortText 不是唯一的
        System.out.println("handlerAdded 被调用 "+ctx.channel().id().asLongText());
        System.out.println("handlerAdded 被调用 "+ctx.channel().id().asShortText());
    }



    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemove 调用 "+ctx.channel().id().asLongText());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生 "+cause.getMessage());
        ctx.close();  //关闭连接
    }
}
