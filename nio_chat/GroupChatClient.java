package com.xizi.nio_chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class GroupChatClient {

    //定义相关的属性
    private final String HOST="127.0.0.1";
    private final  int PORT=6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private  String username;

    //构造器
    public GroupChatClient() throws IOException {
        selector=Selector.open();
        //连接服务器
        socketChannel=SocketChannel.open(new InetSocketAddress(HOST,PORT));
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //将channel 注册到selector
        socketChannel.register(selector, SelectionKey.OP_READ);
        //得到username
         username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username+" 客户端启动成功.....");

    }

    //向服务器发送消息
    public void sendInfo(String info){
        info=username+"说： "+info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //读取从服务器端回复的消息
    public  void readInfo(){
        try {
            int readChannel = selector.select();
            if(readChannel>0){
                //有可以用的通道
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while(keyIterator.hasNext()){
                    SelectionKey key = keyIterator.next();
                    if(key.isReadable()){
                        //得到相关的通道
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        //设置SocketChannel为非阻塞
                        socketChannel.configureBlocking(false);
                        //读取数据到缓冲区
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        socketChannel.read(buffer);
                        //把缓冲区的数据转成字符串
                        String msg = new String(buffer.array());
                        System.out.println("服务器回复的数据： "+msg.trim());
                    }
                }
                //删除当前的selectionKey 防止重复操作
                keyIterator.remove();
            }else{
//                System.out.println("没有可用的通道...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {
        //q启动客户端
        GroupChatClient groupChatClient = new GroupChatClient();
        //启动一个线程 每隔3秒 读取服务器发送的信息
        new Thread(){
            @Override
            public void run() {
                while(true){
                    groupChatClient.readInfo();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();


        //客户端发送数据给服务端
        Scanner scanner = new Scanner(System.in);
        System.out.println("请发送信息: ");
        while(scanner.hasNextLine()){
            System.out.println("请发送信息: ");
            String s = scanner.nextLine();
            groupChatClient.sendInfo(s);
        }
    }
}
