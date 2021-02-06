package com.xizi.dubboRPC.provider;

import com.xizi.dubboRPC.publicInterface.HelloService;

public class HelloServiceImpl implements HelloService {
    private static int count=0;

    //当消费方调用该方法就返回一个结果
    @Override
    public String hello(String mes) {
        System.out.println("收到客户端消息="+mes);
        if(mes!=null){
            return "你好客户端,我已经收到的消息 ["+mes+"]第"+(++count)+"次";
        }
        return "你好客户端,我已经收到的消息";
    }
}
