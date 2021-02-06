package com.xizi.inboundhandlerAndoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MyLongToByteEncoder extends MessageToByteEncoder<Long> {

    //编码  客户端发送数据给服务端 进行编码
    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
        System.out.println("MyLongToByteEncoder的encode编码 被调用");
        System.out.println("msg="+msg);
        out.writeLong(msg);
    }
}
