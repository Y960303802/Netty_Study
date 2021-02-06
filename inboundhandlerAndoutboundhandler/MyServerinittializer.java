package com.xizi.inboundhandlerAndoutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyServerinittializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //接收客户端发送来的信息 入站的handler进行解码 MyByteToLongDecoder
//        pipeline.addLast(new MyByteToLongDecoder());
        pipeline.addLast(new MyByteToLongDecoder2());
        //服务器发送信息给客户端  出站得handler进行编码  MyLongToByteEncoder
        pipeline.addLast(new MyLongToByteEncoder());
        //自定义得handler 处理业务逻辑
        pipeline.addLast(new MyServerhandler());
    }
}
