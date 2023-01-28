package com.lgy.socket.core.common;

import com.lgy.socket.core.bean.PkgDataBean;

/**
 * 发送数据
 */
public interface TransferAction {
    <T> void send(T data) throws Exception;
}
