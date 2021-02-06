package com.xizi.netty_buf;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class NettyByteBuf02 {
    public static void main(String[] args) {
        //创建ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("123456789", Charset.forName("utf-8"));

        //使用相关的方法
        if(byteBuf.hasArray()){
            byte[] array = byteBuf.array();
            //转为字符串
            System.out.println(new String(array,Charset.forName("utf-8")));
            System.out.println("byteBuffer= "+byteBuf);

            int len=byteBuf.readableBytes();
            System.out.println("可读取的字节数= "+len);
            for (int i = 0; i < len; i++) {
                System.out.print((char)byteBuf.getByte(i)+" ");
            }

        }

    }
}
