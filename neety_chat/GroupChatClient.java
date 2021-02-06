package com.xizi.neety_chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class GroupChatClient {
 private  final String host;
 private final int port;

 public GroupChatClient(String host,int port){
     this.host=host;
     this.port=port;
 }

 public void run(){
     NioEventLoopGroup group = new NioEventLoopGroup();
     try {
         //客户端启动器
         Bootstrap bootstrap = new Bootstrap();

         bootstrap.group(group)
                 .channel(NioSocketChannel.class)
                 .handler(new ChannelInitializer<SocketChannel>() {

                     @Override
                     protected void initChannel(SocketChannel ch) throws Exception {
                         //得到pipline
                         ChannelPipeline pipeline = ch.pipeline();
                         //加入相关的handler
                         pipeline.addLast("decoder",new StringDecoder());
                         pipeline.addLast("encoder",new StringEncoder());
                         //加入自定义handler
                         pipeline.addLast(new GroupChatClientHandler());
                     }
                 });

         ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
         Channel channel = channelFuture.channel();
         System.out.println("-------"+channel.localAddress()+"---------");
         //客户端输入信息 创建一个扫描器
         Scanner sc = new Scanner(System.in);
         while(sc.hasNextLine()){
             String msg = sc.nextLine();
             //通过channel 发送到服务器端
             channel.writeAndFlush(msg+"\n");
         }

     } catch (InterruptedException e) {
         e.printStackTrace();
     } finally {
        group.shutdownGracefully();
     }
 }

    public static void main(String[] args) {
        new GroupChatClient("127.0.0.1", 7000).run();
    }

}
