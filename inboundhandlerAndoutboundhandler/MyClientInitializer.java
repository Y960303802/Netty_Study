package com.xizi.inboundhandlerAndoutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //加入一个出站的handler 对数据进行一个编码  MyLongToByteEncoder
        pipeline.addLast(new MyLongToByteEncoder());
        //加入一个入站得handler 对数据进行一个解码  MyByteToLongDecoder
//        pipeline.addLast(new MyByteToLongDecoder());

        pipeline.addLast(new MyByteToLongDecoder());

        //加入一个自定义handler 处理业务
        pipeline.addLast(new MyClientHandler());
    }
}
