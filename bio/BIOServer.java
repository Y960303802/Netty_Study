package com.xizi.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {
    public static void main(String[] args) throws Exception {
        //1.创建一个线程池
        //2.若有客户端连接 就创建一个线程进行连接
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        //创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动~~~~");

        while(true){
            //监听 等待客户端连接
            System.out.println("等待连接~~~~~~~");
            final Socket accept = serverSocket.accept();
            System.out.println("连接到一个客户端");
            newCachedThreadPool.execute(new Runnable() {
                public void run() {
                    handler(accept);
                }
            });
        }
    }

    //编写一个handler方法 和客户端通信
    public static void handler(Socket socket){
        try {
            System.out.println("线程信息 id ="+Thread.currentThread().getId());
            System.out.println("线程名称 name ="+Thread.currentThread().getName());
            System.out.println("-----------------------------------------------------");
            byte[] bytes=new byte[1024];
            //通过socket 获取输入流
            InputStream inputStream = socket.getInputStream();
            //循环的读取客户端发送的数据
            while(true){
                System.out.println("开始读取数据~~~~~~~~~~~~");
                System.out.println("线程信息 id ="+Thread.currentThread().getId());
                System.out.println("线程名称 name ="+Thread.currentThread().getName());
                System.out.println("read~~~~~~~~~~~~~~");
                System.out.println("-----------------------------------------------------");
                int read = inputStream.read(bytes);
                if (read!=-1){
                    String s = new String(bytes, 0, read);
                    //打印客户端发送的数据
                    System.out.println(s);
                }else{
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭client的连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
