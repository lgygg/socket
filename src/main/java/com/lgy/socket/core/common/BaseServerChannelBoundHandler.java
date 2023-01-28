package com.lgy.socket.core.common;


import com.lgy.socket.core.bean.PkgDataBean;
import com.lgy.socket.core.service.TransferType;
import com.lgy.socket.util.LogHelper;

import io.netty.channel.ChannelHandlerContext;

public abstract class BaseServerChannelBoundHandler<T> extends ChannelHandler<PkgDataBean<T>> {

    public BaseServerChannelBoundHandler(BoundHandler<PkgDataBean<T>> handler) {
        super(handler);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PkgDataBean<T> msg) throws Exception {
        if (msg.getCmd() == TransferType.CLIENT_HEART_HIT.getKey()) {
            LogHelper.d("服务器获取客户端心跳:"+msg.getData());
            PkgDataBean<String> bean = new PkgDataBean();
            //响应客户端心跳
            bean.setCmd(TransferType.SERVER_HEART_HIT.getKey());
            bean.setData("服务器心跳");
            bean.setDataLength((byte) bean.getData().getBytes().length);
            ctx.channel().writeAndFlush(bean);
        }else{
            super.channelRead0(ctx,msg);
            this.channelRead1(ctx,msg);
        }
    }

    protected abstract void channelRead1(ChannelHandlerContext ctx, PkgDataBean<T> msg);

}