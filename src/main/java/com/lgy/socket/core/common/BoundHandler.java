package com.lgy.socket.core.common;

import io.netty.channel.Channel;
/**
 * 监听连接和断开情况，监听数据的读取
 *
 * @author: Administrator
 * @date: 2024/5/21
 */
public interface BoundHandler<T> {
    void read(String ip,Integer port,T t);
    void channelActive(String ip, Integer port, Channel channel);
    void channelInactive(String ip,Integer port, Channel channel);
}
