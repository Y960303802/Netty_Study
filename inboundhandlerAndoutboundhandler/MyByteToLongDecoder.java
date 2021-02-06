package com.xizi.inboundhandlerAndoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {

    /**
     *
     * @param ctx  上下文对象
     * @param in    入站的ByteBuf
     * @param out  list集合 将解码后的数据传给下一个handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyByteToLongDecoder的decode 解码被调用");
        //大于等于8个字节才能读取一个Long
        if (in.readableBytes()>=8){
            out.add(in.readLong());
        }
    }
}
