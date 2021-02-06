package com.xizi.codec2;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Random;

public class NeetyClientHandler extends ChannelInboundHandlerAdapter {
    //当通道就绪时就会触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    //随机发送发送一个Student/Worker 对象到服务器
        int random =   new Random().nextInt(3);
        MyDataInfo.MyMessage myMessage=null;

        if(random==0){// 发送Student 对象
            myMessage= MyDataInfo.MyMessage.newBuilder().setDataType(
                    MyDataInfo.MyMessage.DataType.StudentType).setStudent(MyDataInfo.Student.newBuilder()
                    .setId(6).setName("戏子66666").build()).build();
        }else{ //发送一个Worker对象
            myMessage= MyDataInfo.MyMessage.newBuilder().setDataType(
                    MyDataInfo.MyMessage.DataType.WorkerType).setWorker(MyDataInfo.Worker.newBuilder()
                    .setAge(18).setName("戏子77777").build()).build();
        }
        ctx.writeAndFlush(myMessage);

    }

    //当通道有读取事件 会触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf= (ByteBuf) msg;
        System.out.println("服务器回复的消息："+buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址："+ctx.channel().remoteAddress());
    }

    //异常发生
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
    }
}
