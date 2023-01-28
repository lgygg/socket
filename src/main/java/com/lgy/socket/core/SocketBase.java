package com.lgy.socket.core;

import com.lgy.socket.core.service.State;

/**
 * socket基础信息
 */
public class SocketBase {
    protected Integer port;
    protected String ip;
    protected State currentState = State.INIT;

    public SocketBase(){}

    public SocketBase(String ip, Integer port){
        this.port = port;
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }
}
