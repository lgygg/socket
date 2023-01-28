package com.lgy.socket.core.common;

import android.text.TextUtils;

import com.lgy.socket.core.SocketBase;
import com.lgy.socket.core.bean.PkgDataBean;
import com.lgy.socket.core.service.TransferType;
import com.lgy.socket.util.Global;
import com.lgy.socket.util.LogHelper;

import java.net.SocketAddress;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public abstract class BaseClientChannelBoundHandler<T> extends ChannelHandler<T> {
    public BaseClientChannelBoundHandler(BoundHandler<T> handler) {
        super(handler);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            if (((IdleStateEvent) evt).state() == IdleState.READER_IDLE) {
                sendHeartPkg(ctx);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }


    /**
     * 发送心跳
     */
    private void sendHeartPkg(ChannelHandlerContext ctx) {
        if (Global.isSendHeartBeat != 2) {
            PkgDataBean<String> bean = new PkgDataBean();
            bean.setCmd(TransferType.CLIENT_HEART_HIT.getKey());
            bean.setData("客户端心跳数据包");
            bean.setDataLength((byte) bean.getData().getBytes().length);
            ctx.channel().writeAndFlush(bean);
        }
    }

}