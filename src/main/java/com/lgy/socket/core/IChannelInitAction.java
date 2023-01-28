package com.lgy.socket.core;

import com.lgy.socket.core.common.BaseChannelInitializer;

import io.netty.channel.socket.SocketChannel;

/**
 * 配置ChannelInitializer
 */
public interface IChannelInitAction {
    void setChannelInitializer(BaseChannelInitializer<SocketChannel> initializer);
}
