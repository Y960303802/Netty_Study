package com.xizi.netty_buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuf01 {
    public static void main(String[] args) {
        //创建一个ByteBuf
        //1. 创建一个对象 该对象包含一个数组  是一个byte[10]
        //2.NIO buffer中不需要使用flip进行反转
        ByteBuf buffer = Unpooled.buffer(10);
        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }
        System.out.println("capacity= "+buffer.capacity());

        for (int i = 0; i < buffer.capacity(); i++) {
//            System.out.print(buffer.getByte(i)+" ");
            System.out.println(buffer.readerIndex(i));
        }
    }
}
