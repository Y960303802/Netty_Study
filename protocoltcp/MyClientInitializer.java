package com.xizi.protocoltcp;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //加入一个编码器
        pipeline.addLast(new MyMessageEncoder());
        //加入一个解码器
        pipeline.addLast(new MyMessageDecoder());
        //加入一个自定义handler 处理业务
        pipeline.addLast(new MyClientHandler());
    }
}
