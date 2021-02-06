package com.xizi.dubboRPC.customer;

import com.xizi.dubboRPC.netty.NettyClient;
import com.xizi.dubboRPC.publicInterface.HelloService;

public class ClientBootstrap {

    //定义协议头
    public static final String providerName="hello#";

    public static void main(String[] args) throws InterruptedException {
        //创建一个消费者
        NettyClient customer = new NettyClient();
        //创建一个代理对象
        HelloService service = (HelloService) customer.getBean(HelloService.class, providerName);

        for (;;){
            Thread.sleep(6);
            //通过代理对象调用提供者的方法
            String res = service.hello("戏子666");
            System.out.println("调用的结果 res= "+res);
        }
    }
}
