package com.lgy.socket.core.common;

import com.lgy.socket.core.bean.TransferData;

/**
 * 连接状态的监听
 * @author: Administrator
 * @date: 2024/5/21
 */
public interface StateCallback<T> {
    void setCallBack(CallBack<T> callBack);
}
