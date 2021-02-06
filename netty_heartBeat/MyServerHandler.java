package com.xizi.netty_heartBeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class MyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     *
     * @param ctx  上下文
     * @param evt  事件
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            //将 evt向下转型
            IdleStateEvent event=(IdleStateEvent) evt;
            String evenType=null;
            switch (event.state()){
                case READER_IDLE:
                    evenType="读空闲";
                    break;
                case WRITER_IDLE:
                    evenType="写空闲";
                case ALL_IDLE:
                    evenType="读写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress()+"超时事件发送："+evenType);

            //出现空闲 关闭通道测试 不会死循环进行 只会读一次测试
            ctx.channel().close();

        }

    }
}
