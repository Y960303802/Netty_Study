package com.xizi.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel_03 {
    public static void main(String[] args) throws Exception {
        //直接在当前的项目下创建文件加进行读取
        FileInputStream fileInputStream = new FileInputStream("input.txt");
        FileChannel fileChannel01 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("output.txt");
        FileChannel fileChannel02 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while(true){
            byteBuffer.clear(); //清空buffer 重置标记位
            int read = fileChannel01.read(byteBuffer);
            if(read==-1){
                break;
            }else{
                byteBuffer.flip();
                fileChannel02.write(byteBuffer);
            }
        }
        fileInputStream.close();
        fileOutputStream.close();
    }
}
