package com.lgy.socket.core.common;

import io.netty.channel.Channel;

public interface BoundHandler<T> {
    void read(String ip,Integer port,T t);
    void channelActive(String ip, Integer port, Channel channel);
    void channelInactive(String ip,Integer port, Channel channel);
}
