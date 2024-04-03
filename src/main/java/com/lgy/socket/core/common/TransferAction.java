package com.lgy.socket.core.common;

/**
 * 发送数据
 */
public interface TransferAction {
    <T> void send(T data) throws Exception;
}
