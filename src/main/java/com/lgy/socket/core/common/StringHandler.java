package com.lgy.socket.core.common;

import android.text.TextUtils;

import com.lgy.socket.core.SocketBase;
import com.lgy.socket.core.bean.PkgDataBean;
import com.lgy.socket.util.LogHelper;

import java.net.SocketAddress;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class StringHandler extends SimpleChannelInboundHandler<PkgDataBean> {

    private BoundHandler<PkgDataBean<String>> handler;
    public StringHandler(BoundHandler<PkgDataBean<String>> handler) {
        super();
        this.handler = handler;
    }

    /**
     * 当收到数据的回调
     *
     * @param channelHandlerContext 封装的连接对像
     * @param bean
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, PkgDataBean bean) throws Exception {
        if (this.handler != null) {
            SocketAddress socketAddress = channelHandlerContext.channel().remoteAddress();
            SocketBase socketBase = dealAddress(socketAddress);
            this.handler.read(socketBase.getIp(),socketBase.getPort(),bean);
        }
        LogHelper.d("收到了解码器处理过的数据：" + bean.toString());
    }

    /**
     * 有客户端连接过来的回调
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        if (this.handler != null) {
            SocketAddress socketAddress = ctx.channel().remoteAddress();
            SocketBase socketBase = dealAddress(socketAddress);
            this.handler.channelActive(socketBase.getIp(),socketBase.getPort(),ctx.channel());
        }
        LogHelper.d("有客户端连接过来：" + ctx.toString());
    }

    /**
     * 有客户端断开了连接的回调
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        if (this.handler != null) {
            SocketAddress socketAddress = ctx.channel().remoteAddress();
            SocketBase socketBase = dealAddress(socketAddress);
            this.handler.channelInactive(socketBase.getIp(),socketBase.getPort(),ctx.channel());
        }
        LogHelper.d("有客户端断开了连接：" + ctx.toString());
    }


    private SocketBase dealAddress(SocketAddress socketAddress){
        SocketBase socketBase = new SocketBase();
        if (socketAddress != null) {
            String address = socketAddress.toString();
            if (!TextUtils.isEmpty(address)) {
                String[] addr = address.replace("/","").split(":");
                if (addr.length>1) {
                    socketBase.setIp(addr[0]);
                    socketBase.setPort(Integer.valueOf(addr[1]));
                }
            }
        }
        return socketBase;
    }

}
