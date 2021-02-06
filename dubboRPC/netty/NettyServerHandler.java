package com.xizi.dubboRPC.netty;

import com.xizi.dubboRPC.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/*
 1. 自定义Handler 继承ChannelInboundHandlerAdapter
 */

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /*
        1. ChannelHandlerContext 上下文对象 含有管道pipline 通道channel 地址
        2. msg 客户端发送的数据
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送的信息 并调用服务
        System.out.println("msg="+msg);
        //客户端在调用服务器的api时 需要定一个协议
        //要求必须以某个字符串开头
        if(msg.toString().startsWith("hello#")){
            String result = new HelloServiceImpl().hello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
            ctx.writeAndFlush(result);
        }
    }


    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

    }

    //处理异常 关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
