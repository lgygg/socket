package com.lgy.socket.core.client;

import com.lgy.socket.core.bean.PkgDataBean;
import com.lgy.socket.core.common.BaseClientChannelBoundHandler;
import com.lgy.socket.core.common.BoundHandler;
import com.lgy.socket.util.LogHelper;

import io.netty.channel.ChannelHandlerContext;

public class ClientStringHandler extends BaseClientChannelBoundHandler<PkgDataBean<String>> {
    public ClientStringHandler(BoundHandler<PkgDataBean<String>> handler) {
        super(handler);
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PkgDataBean<String> msg) throws Exception {
        super.channelRead0(ctx,msg);
        LogHelper.d("客户端-读取信息：" + msg.getData());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        LogHelper.d("客户端-连接服务器：" + ctx.toString());

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        LogHelper.d("客户端-断开链接：" + ctx.toString());

    }
}
