package com.lgy.socket.core.common;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public abstract class BaseChannelInitializer<T> extends ChannelInitializer<SocketChannel> {
    protected BoundHandler<T> handler;

    public void setHandler(BoundHandler<T> handler) {
        this.handler = handler;
    }
}
