package com.xizi.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/*
    NIO非阻塞
 */
public class NIOServer_01 {
    public static void main(String[] args) throws IOException {
        //创建ServerSocketChannel-> ServerSocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个Selector对象
        Selector selector = Selector.open();

        //绑定一个端口 在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设置位非阻塞
        serverSocketChannel.configureBlocking(false);

        //把 serverSocketChannel 注册到 selector 关心事件 OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("注册后的selectionkey 数量="+selector.keys().size());
        //等待客户端连接
        while(true){
            if(selector.select(1000)==0){ //没有时间发生
                System.out.println("服务器等待了一秒，没有连接");
                continue;
            }
            //获取到相关的 selectionkey集合
            //1. 返回大于0，表示已经获取到关注的事件
            //2. selector.selectedKeys() 返回关注事件的集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("selectionKeys 数量="+selectionKeys.size());
            //通过 selectionKeys  反向获取通道
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while(keyIterator.hasNext()){
                //获取到selectionKey
                SelectionKey key = keyIterator.next();
                //根据key 对应的通道发生的事件做相应的处理
                if (key.isAcceptable()){ // 若是OP_ACCEPT 有新的客户端连接
                    //该客户端生成一个 socketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功 生成了一个新的socketChannel "+socketChannel.hashCode());
                    //将socketChannel设置为非阻塞
                    socketChannel.configureBlocking(false);
                    //将socketChannel 注册到selector  关注事件为OP_READ
                    //同时给将socketChannel关联一个buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println("客户端连接后，注册的selectionkey 数量="+selector.keys().size());

                }
                if (key.isReadable()){ //发生 OP_READ
                    //通过key 反向获取对应的channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    //获取到channel 关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.out.println("客户端发送信息："+new String(buffer.array()));
                }

                //手动从集合中移除当前的selectionKey,防止重复操作
                keyIterator.remove();
            }
        }
    }
}
