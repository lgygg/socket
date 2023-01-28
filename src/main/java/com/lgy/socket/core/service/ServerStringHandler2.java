package com.lgy.socket.core.service;

import com.lgy.socket.core.bean.PkgDataBean;
import com.lgy.socket.util.LogHelper;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerStringHandler2 extends SimpleChannelInboundHandler<PkgDataBean<String>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PkgDataBean<String> msg) throws Exception {
        LogHelper.d("服务器-响应客户端心跳"+msg.getCmd());
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
