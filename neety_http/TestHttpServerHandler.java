package com.xizi.neety_http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

// SimpleChannelInboundHandler 是ChannelInboundHandlerAdapter 子类
// HttpObject 客户端和服务端相互通讯的数据被封装成HttpObject
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    //channelRead0 读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //判断msg 是不是httpRequest
        if(msg instanceof HttpRequest){

            System.out.println("pipeline hashcode= "+ctx.pipeline().hashCode());
            System.out.println("TestHttpServerHandler= "+this.hashCode());

            System.out.println("msg类型= "+msg.getClass());
            System.out.println("客户端地址 "+ctx.channel().remoteAddress().toString());

            HttpRequest httpRequest=(HttpRequest)msg;
            //获取uri
            URI uri = new URI(httpRequest.uri());
            if("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求了 favicon.ico ，不做响应");
                return;
            }
            System.out.println("--------------------------------------");

            //回复信息给浏览器[http协议]
            ByteBuf content = Unpooled.copiedBuffer("Hello,我是服务器", CharsetUtil.UTF_8);
            //构造一个http的响应 httpResponse
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain;charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());


            //将构建好的response 返回
            ctx.writeAndFlush(response);
        }
    }
}
