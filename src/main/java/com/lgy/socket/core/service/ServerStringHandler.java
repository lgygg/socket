package com.lgy.socket.core.service;

import com.lgy.socket.core.bean.PkgDataBean;
import com.lgy.socket.core.common.BaseServerChannelBoundHandler;
import com.lgy.socket.core.common.BoundHandler;
import com.lgy.socket.util.LogHelper;

import io.netty.channel.ChannelHandlerContext;

public class ServerStringHandler extends BaseServerChannelBoundHandler<String> {
    public ServerStringHandler(BoundHandler<PkgDataBean<String>> handler) {
        super(handler);
    }

    /**
     * 当收到数据的回调
     *
     * @param ctx 封装的连接对像
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead1(ChannelHandlerContext ctx, PkgDataBean<String> msg) {
        LogHelper.d("服务器-读取数据：" + msg.getData());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        LogHelper.d("服务器-连接服务器：" + ctx.toString());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        LogHelper.d("服务器-断开链接：" + ctx.toString());
    }
}
