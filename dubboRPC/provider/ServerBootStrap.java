package com.xizi.dubboRPC.provider;

        import com.xizi.dubboRPC.netty.NettyServer;

//ServerBootStrap 会启动一个服务提供者 就是 NettyServer
public class ServerBootStrap {
    public static void main(String[] args) {
        NettyServer.startServer("127.0.0.1", 7000);
    }
}
