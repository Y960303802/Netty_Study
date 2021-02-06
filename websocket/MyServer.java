package com.xizi.websocket;

import com.xizi.netty_heartBeat.MyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;


public class MyServer {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))//添加一个日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //基础http协议 使用http解码/编码器
                            pipeline.addLast(new HttpServerCodec());
                            //以块方式写，添加CHunkedWritehandler 处理器
                            pipeline.addLast(new ChunkedWriteHandler());
                            // 1. http数据传输过程中是分段，HttpObjectAggregator 就是将多个分段聚合起来
                            // 2. 当数据量很大时候，就会发送多次http请求
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            //1. 对应websocket  它的数据是以 帧(frame) 形式传递
                            //2. 浏览器发送请求 ws://localhost:7000/xxx  表示请求的uri
                            //3. WebSocketServerProtocolHandler 核心功能就是将http协议升级为 ws协议 保持长连接
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            //自定义的handler 处理业务逻辑
                            pipeline.addLast(new MyTextWebSocketFrameHandler() );
                        }
                    });

            //启动服务器
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
