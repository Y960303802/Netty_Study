package com.xizi.nio_chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class GroupChatServer {
    //定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int  PORT=6667;


    public GroupChatServer() {
        try {
            //得到选择器
            selector=Selector.open();
            //得到ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //将listenChannel设置为非阻塞模式
            listenChannel.configureBlocking(false);
            //将将listenChannel 注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //监听
    public void listen(){
        while (true){
            try {
                //使用选择器监听
                int count = selector.select();
                if(count>0){ //有事件处理
                    //遍历得到的selectionKey 集合
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while(keyIterator.hasNext()){{
                        //取出selectionkey
                        SelectionKey key = keyIterator.next();
                        //监听到accept事件
                        if(key.isAcceptable()){
                            SocketChannel socketChannel = listenChannel.accept();
                            //设置SocketChannel为非阻塞
                            socketChannel.configureBlocking(false);
                            //将socketChannel注册到selector
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            //提示
                            System.out.println(socketChannel.getRemoteAddress()+" 上线");
                        }
                        //监听到read事件 通道可读
                        if (key.isReadable()){
                            //处理读
                            readData(key);
                        }

                        //当前的key 删除 防止重复处理
                        //手动从集合中移除当前的selectionKey,防止重复操作
                        keyIterator.remove();
                    }}

                }else {
                    System.out.println("等待.............");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
        }
    }

    //读取客户端消息
    public  void  readData(SelectionKey key){
        //定义一个SocketChannel
        SocketChannel channel=null;
        try {
             channel =(SocketChannel)key.channel();
             //创建buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            //将通道的信息读取到缓冲区
            int count = channel.read(buffer);
            if(count>0){
                //把缓冲区数据转成字符串
                String msg = new String(buffer.array());
                System.out.println("客户端发送的信息是："+msg);
                //向其他客户端转发消息(去掉自己)
                sendInfoToOTHERClient(msg, channel);
            }
        } catch (Exception e) {
            try {
                System.out.println(channel.getRemoteAddress()+" 离线了");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    //向其他客户端转发消息（给通道发）
    public void sendInfoToOTHERClient(String msg,SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中.......");
        //遍历 所有注册到selector 上的SocketChannel 排除本身
        for (SelectionKey key:selector.keys()) {
            //通过key 取出对应的SocketChannel
            Channel targetChannel = key.channel();
            //排除自己
            if (targetChannel instanceof SocketChannel && targetChannel!=self){
                SocketChannel dest=(SocketChannel)targetChannel;
                //将msg存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                dest.write(buffer);
            }

        }
    }



    public static void main(String[] args) {
        //创建一个服务器对象
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }

}
