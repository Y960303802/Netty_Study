package com.xizi.neety;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
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
        // 将耗时的业务-> 异步执行0》提交到channel对应的NioEnventLoop 的taskQueue中
//        Thread.sleep(10*1000);
//        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,客户端777",CharsetUtil.UTF_8));
        //方法一 用户程序自定义普通任务
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,客户端666",CharsetUtil.UTF_8));

                } catch (InterruptedException e) {
                    System.out.println("发生异常"+e.getMessage());
                }
            }
        });
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(12*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,客户端777",CharsetUtil.UTF_8));

                } catch (InterruptedException e) {
                    System.out.println("发生异常"+e.getMessage());
                }
            }
        });

        //用户自定义定时任务---> 该任务是提交到scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(12*1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,客户端9999",CharsetUtil.UTF_8));

                } catch (InterruptedException e) {
                    System.out.println("发生异常"+e.getMessage());
                }

            }
        },5, TimeUnit.SECONDS);


       System.out.println("go on.........");

//        System.out.println("服务器读取线程 "+Thread.currentThread().getName());
//        System.out.println("Server ctc= "+ctx);
//        System.out.println("看看channel和pipeLine的关系");
//        Channel channel = ctx.channel();
//        ChannelPipeline pipeline = ctx.pipeline(); //本质是一个双向链表
//        //将msg转为ByteBuf
//        ByteBuf buf=(ByteBuf)msg;
//        System.out.println("客户端发送的消息是： "+buf.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端地址："+ctx.channel().remoteAddress());
    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入到缓存 并刷新
        //将发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,客户端888",CharsetUtil.UTF_8));
    }

    //处理异常 关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
