package com.xizi.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public class ScatteringAndGathering {
    /**
     * Scattering : 将数据写入到buffer时，可以采用buffer数组 依次写入
     * Gathering :从buffer读取数据时，可以采用buffer数组，依次读
     *
     */
    public static void main(String[] args) throws Exception {

        //使用 ServerSocketChannel 和SocketChannel 网络
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
        //绑定端口到socket,并启动
        serverSocketChannel.socket().bind(inetSocketAddress);
        //创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0]=ByteBuffer.allocate(5);
        byteBuffers[1]=ByteBuffer.allocate(3);

        //等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();
        //假设从客户端接收8个字节
        int messageLength=8;
        //循环的读取
        while(true){
            int byteRead=0;
            while(byteRead<messageLength){
                long l = socketChannel.read(byteBuffers);
                //累计读取的字节数
                byteRead+=l;
                System.out.println("byteRead="+byteRead);
                //使用流打印
                Arrays.asList(byteBuffers).stream().map(buffer->"postion="+
                        buffer.position()+",limit="+buffer.limit()).forEach(System.out::println);
            }

            //将所有的buffer进行flip
                Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.flip());

                //将数据回显到客户端
                long byteWrite=0;
                while(byteWrite<messageLength){
                    long len2 = socketChannel.write(byteBuffers);
                    byteWrite+=len2;
                }

                //将所有的buffer 进行clear
            Arrays.asList(byteBuffers).forEach(byteBuffer -> {
                byteBuffer.clear();
            });

            System.out.println("byteRead="+byteRead+" byteWrite="+byteWrite+
                    " messageLength="+messageLength);


        }
    }
}
