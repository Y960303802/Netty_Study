package com.xizi.neety_http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //向管道加入处理器
        //得到管道
        ChannelPipeline pipeline = ch.pipeline();

        //加入一个neety提供的httpServerCodec  =>[coder -decoder]
        // HttpServerCodec 是neety 提供的处理的http的 编码解码器
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());

        //增加一个自定义的handler
        pipeline.addLast("MyTestHttpServerHandler",new TestHttpServerHandler());

        System.out.println("ok......................");
    }
}
