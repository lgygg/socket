package com.lgy.socket.core.common;

import com.lgy.socket.core.service.State;

public interface CallBack<T> {

    void onState(State state, T msg);
}
