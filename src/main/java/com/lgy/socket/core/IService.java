package com.lgy.socket.core;

public interface IService {

    // 等待链接
    void listen();
    // 客户端连接服务端成功的时候触发
    void accept();
    // 关闭服务
    void close();
}
