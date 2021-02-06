package com.xizi.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/*
 1. 自定义Handler 继承ChannelInboundHandlerAdapter
 */

public class NeetyServerHandler extends ChannelInboundHandlerAdapter {

    /*
        1. ChannelHandlerContext 上下文对象 含有管道pipline 通道channel 地址
        2. msg 客户端发送的数据
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //读取从客户端发送的StudentPojo.Student
        StudentPOJO.Student student= (StudentPOJO.Student) msg;
        System.out.println("客户端发送的数据 id="+student.getId());
        System.out.println("客户端发送的数据 name="+student.getName());
    }
    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入到缓存 并刷新
        //将发送的数据进行编码
//        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,客户端888",CharsetUtil.UTF_8));
    }

    //处理异常 关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
