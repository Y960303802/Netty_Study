package com.xizi.nio;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedByteBuffe_Test {
    /*
    1. mappedByteBuffer 可以让文件直接在内存修改，操作系统不需要拷贝一次
     */
    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("11.txt", "rw");
        //获取对应的通道
        FileChannel channel = randomAccessFile.getChannel();
        /**
         * 参数1：使用读写模式
         * 参数2：0 可以直接修改的起始位置
         * 参数3： 5 是映射到内存的大小  将多少个字节映射到内存
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0,(byte)'X' );
        mappedByteBuffer.put(3,(byte)'9' );

        randomAccessFile.close();
    }

}
