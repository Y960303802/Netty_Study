package com.xizi.neety_chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;


public class GroupChatServerhandler extends SimpleChannelInboundHandler<String> {

    //定义一个channel组 管理所有的channel
    // GlobalEventExecutor.INSTANCE 是全局的事件执行器，是一个单例
    private static ChannelGroup channelGroup =new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    //时间
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



    //客户端连接建立 一旦被连接 第一个被执行
    //将当前的channel加入到channeGroup
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给其他在线的客户端
        //该方法会将 channelGroup 中所有的channel遍历并发送消息
        channelGroup.writeAndFlush("[客户端]-"+"-["+simpleDateFormat.format(new Date())+"]-"+channel.remoteAddress()+" 加入聊天\n");
        channelGroup.add(channel);
    }


    //断开连接,将xx客户离开的信息推送给当前的在线客户
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]-"+"-["+simpleDateFormat.format(new Date())+"]-"+channel.remoteAddress()+"离开了");
        System.out.println("当前ChannelGroup的大小= "+channelGroup.size());
    }

    //表示channel 处于活跃的状态 提示上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("["+simpleDateFormat.format(new Date())+"]-"+ctx.channel().remoteAddress()+" 上线了......");
    }

    //表示channel 处于离线的状态 提示上离线
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("["+simpleDateFormat.format(new Date())+"-]-"+ctx.channel().remoteAddress()+" 离线了.........");
    }

    //读取客户端发送的信息 转发给在线的所有人
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //获取到channel
        Channel channel = ctx.channel();
        channelGroup.forEach(ch->{
            if(channel!=ch){ //不是当前的channel 直接转发
                ch.writeAndFlush(simpleDateFormat.format(new Date())+"-"+"[客户]"+channel.remoteAddress()+"发送消息"+msg+"\n");
            }else{
                ch.writeAndFlush(simpleDateFormat.format(new Date())+"-"+"[自己]发送了消息: "+msg+"\n");
            }
        });
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
