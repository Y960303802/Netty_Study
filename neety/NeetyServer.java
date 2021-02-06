package com.xizi.neety;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.ChannelGroupFutureListener;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NeetyServer {
    public static void main(String[] args) throws InterruptedException {
        //创建BossGroup 和WorkerGroup
        //1. 创建两个线程bossGroup 和workerGroup
        //2. bossGroup 只是处理连接请求 真正的和客户端业务处理 交给WorkerGroup完成
        //3. 两个都是无线循环
        //4. boosGroup 和workerGroup 含有的子线程(NioEventLoop) 数量 默认是 cpu处理器*2
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //创建服务器端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            //使用链式编程来进行设置
            bootstrap.group(bossGroup,workerGroup) //设置两个线程组
                    .channel(NioServerSocketChannel.class) //NioServerSocketChannel 作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) //设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true ) //设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {  //创建一个通道测试对象
                        // 给pipline 设置处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            System.out.println("客户端socketchannel hashcode"+socketChannel.hashCode());
                            socketChannel.pipeline().addLast(new NeetyServerHandler());
                        }
                    }); //给workerGroup的EventLoop 对应的管道设置处理器
            System.out.println("服务器准备好了。。。。。。。。。。");

            //绑定一个端口并且同步 生成一个ChannelFuture对象
            ChannelFuture cf = bootstrap.bind(6668).sync();

            //给cf注册监听器
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(cf.isSuccess()){
                        System.out.println("监听端口  6668 成功");
                    }else{
                        System.out.println("监听端口 6668 失败");
                    }
                }
            });
            //关闭通道进行监听
            cf.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //优雅的关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
