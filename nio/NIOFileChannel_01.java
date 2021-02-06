package com.xizi.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel_01 {
    public static void main(String[] args) throws Exception {
        //创建一个输出流->  channel
        FileOutputStream fileOutputStream = new FileOutputStream("1.txt");

        //通过 fileOutPutStream 获取对应的FileChannel
        FileChannel fileChannel = fileOutputStream.getChannel();

        //创建一个缓冲区 bytebuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        String str="戏子6666";
        //将str放入byteBuffer
        byteBuffer.put(str.getBytes());

        //对byteBuffer 进行filp
        byteBuffer.flip();

        //将byteBuffer 数据写入到 fileChannel
        fileChannel.write(byteBuffer);
        fileOutputStream.close();
    }
}
