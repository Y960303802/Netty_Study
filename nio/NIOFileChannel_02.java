package com.xizi.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel_02 {
    public static void main(String[] args) throws Exception {
        File file = new File("1.txt");
        //创建文件的输入流
        FileInputStream fileInputStream = new FileInputStream(file);

        //通过fileInputStream 获取对应的fileChannel
        FileChannel fileChannel = fileInputStream.getChannel();

        //创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        //将通道的数据输入到缓冲区
        fileChannel.read(byteBuffer);

        //将byteBuffer的字节数据转为String
        System.out.println(new String(byteBuffer.array()));
    }
}
