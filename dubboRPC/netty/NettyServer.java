package com.xizi.dubboRPC.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyServer {

    public static void startServer(String hostname,int port){
        startServer0(hostname, port);
    }

    //编写一个方法 完成对NettyServer的初始化和启动
    private  static  void startServer0(String hostname,int port){
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //创建服务器端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            //使用链式编程来进行设置
            bootstrap.group(bossGroup,workerGroup) //设置两个线程组
                    .channel(NioServerSocketChannel.class) //NioServerSocketChannel 作为服务器的通道实现
                    .childHandler(new ChannelInitializer<SocketChannel>() {  //创建一个通道测试对象
                        // 给pipline 设置处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //给workerGroup的EventLoop 对应的管道设置处理器
                            //使用netty的编码解码
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new StringEncoder());
                            //添加业务处理器
                            socketChannel.pipeline().addLast(new NettyServerHandler());

                        }
                    });


            //绑定一个端口并且同步 生成一个ChannelFuture对象
            ChannelFuture cf = bootstrap.bind(hostname,port).sync();

            System.out.println("服务提供方开始提供服务~~~");
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
