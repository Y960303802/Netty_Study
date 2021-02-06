package com.xizi.nio;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        // Buffer使用
        //创建一个Buffer 大小为5 存5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);
        //向buffer 存放数据
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i*2);
        }
        //buffer读取数据 读写切换
        intBuffer.flip();
        intBuffer.position(1);
        intBuffer.limit(3);

        while(intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }

    }
}
