package com.xizi.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient_01 {
    public static void main(String[] args) throws IOException {
        //得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞模式
        socketChannel.configureBlocking(false);
        //提供服务器端IP 端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        //连接服务器
        if(!socketChannel.connect(inetSocketAddress)){
            while (!socketChannel.finishConnect()){
                System.out.println("连接需要时间，客户端可以做其他工作");
            }
        }
        //连接成功 发送数据
        String str="戏子66666";
        //包裹一个字节数组到buffer里面去
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        //发送数据 将buffer写入channel
        socketChannel.write(buffer);
        System.in.read();
    }
}
