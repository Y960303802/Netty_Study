package com.xizi.dubboRPC.netty;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyClient {

    //创建一个固定数量线程池
    private  static ExecutorService executor= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static NettyClientHandler client;
    private int count = 0;
    //编写方法使用代理模式，获取一个代理对象

    public Object getBean(final Class<?> serivceClass, final String providerName) {

        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{serivceClass}, (proxy, method, args) -> {

                    System.out.println("(proxy, method, args) 进入...." + (++count) + " 次");
                    //{}  部分的代码，客户端每调用一次 hello, 就会进入到该代码
                    if (client == null) {
                        initClient();
                    }
                    //设置要发给服务器端的信息
                    //providerName 协议头 args[0] 就是客户端调用api hello(???), 参数
                    client.setPara(providerName + args[0]);
                    return executor.submit(client).get();

                });
    }


    //初始化客户端
    private  static  void initClient(){
        client=new NettyClientHandler();
        //创建EventLoopGroup
        //客户端创建一个事件循环组
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            //创建客户端启动对象
            //客户端使用的不是ServerBootStrap 而是bootStrop
            Bootstrap bootstrap = new Bootstrap();
            //设置相关参数
            bootstrap.group(group)   //设置线程组
                    .channel(NioSocketChannel.class) //设置客户端通道的实现类NioSocketChannel
                    .option(ChannelOption.TCP_NODELAY,true ) //不延时
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //使用netty的编码解码
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new StringEncoder());
                            socketChannel.pipeline().addLast(client);
                        }
                    });

            System.out.println("客户端完成............");

            //启动客户端去连接服务器端
            // channelFuture 要分析涉及到neety的异步模型
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 7000).sync();

            //给关闭通道进行监听
//            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
//            group.shutdownGracefully();
        }
    }

    public static void main(String[] args)  {





    }
}
