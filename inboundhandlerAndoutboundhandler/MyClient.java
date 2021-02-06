package com.xizi.inboundhandlerAndoutboundhandler;

import com.xizi.neety.NeetyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class MyClient {
    public static void main(String[] args) {
        //客户端创建一个事件循环组
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            //创建客户端启动对象
            //客户端使用的不是ServerBootStrap 而是bootStrop
            Bootstrap bootstrap = new Bootstrap();
            //设置相关参数
            bootstrap.group(group)   //设置线程组
                    .channel(NioSocketChannel.class) //设置客户端通道的实现类
                    .handler(new MyClientInitializer());


            //启动客户端去连接服务器端
            // channelFuture 要分析涉及到neety的异步模型
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 7000).sync();

            //给关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }

    }
}
