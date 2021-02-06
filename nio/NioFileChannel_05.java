package com.xizi.nio;

import java.nio.ByteBuffer;

public class NioFileChannel_05 {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(64);

        //类型化方式放入数据
        buffer.putInt(100);
        buffer.putLong(9);
        buffer.putChar('戏');
        buffer.putShort((short)4);

        //取出
        buffer.flip();
        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getShort());

    }
}
