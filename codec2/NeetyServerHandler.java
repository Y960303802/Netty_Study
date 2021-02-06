package com.xizi.codec2;

import com.xizi.codec.StudentPojo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

/*
 1. 自定义Handler 继承ChannelInboundHandlerAdapter
 */

public class NeetyServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {

    /*
        1. ChannelHandlerContext 上下文对象 含有管道pipline 通道channel 地址
        2. msg 客户端发送的数据
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
        //根据dataType 来显示不同的信息
        MyDataInfo.MyMessage.DataType dataType = msg.getDataType();
        if(dataType==MyDataInfo.MyMessage.DataType.StudentType){
            MyDataInfo.Student student = msg.getStudent();
            System.out.println("学生id="+student.getId()+"学生name="+student.getName());
        }else if(dataType==MyDataInfo.MyMessage.DataType.WorkerType){
            MyDataInfo.Worker worker = msg.getWorker();
            System.out.println("工人名字="+worker.getName()+"工人年龄"+worker.getAge());
        }else{
            System.out.println("传输的类型不对！！！");
        }


    }
    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入到缓存 并刷新
        //将发送的数据进行编码
//        ctx.writeAndFlush(Unpooled.copiedBuffer("Hello,客户端888",CharsetUtil.UTF_8));
    }

    //处理异常 关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
