package com.xizi.dubboRPC.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {
    private ChannelHandlerContext context; //上下文
    private String result; //调用后返回的结果
    private String para;  //客户端调用方法时，传入的参数

    //1
    //与服务器创建成功后就会被调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context=ctx;
    }

    //4
    //当通道有读取事件 调用方法
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result= (String) msg;
        notify(); //唤醒等待的线程
    }

    //异常发生
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
    }


    //3
    //被代理对象调用 发送数据给服务器-->wait--> 等待被唤醒(channelRead)--> 返回结果
    @Override
    public synchronized Object call() throws Exception {
        context.writeAndFlush(para);
        //进行wait
        wait();//等待被唤醒(channelRead) 这个方法获取到服务器的结果后 唤醒
        return result; //服务方返回的结果
    }

    //2
    void setPara(String para){
        this.para=para;

    }
}
