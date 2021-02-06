package com.xizi.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class NIOFileChannel_04 {
    public static void main(String[] args) throws Exception {
        //创建文件流
        FileInputStream fileInputStream = new FileInputStream("aa.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("bb.jpg");

        //获取各个流对应的filechannel
        FileChannel sourchChannel = fileInputStream.getChannel();
        FileChannel destChannel = fileOutputStream.getChannel();

        //使用transferForm 进行拷贝
        destChannel.transferFrom(sourchChannel, 0, sourchChannel.size());
        //关闭相关的通道和流
        sourchChannel.close();
        destChannel.close();
        fileInputStream.close();
        fileOutputStream.close();


    }
}
