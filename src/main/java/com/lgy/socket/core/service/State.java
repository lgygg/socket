package com.lgy.socket.core.service;

public enum State {
    INIT(0, "init"), // 初始状态
    START(1, "start"), CONNECTING(2, "connection"), CONNECTED(3, "connected"), DISCONNECTED(4, "disconnected"),
    END(3, "end");

    private Integer level;
    private String state;

    State(Integer level, String state) {
        this.level = level;
        this.state = state;
    }

    public Integer getLevel() {
        return level;
    }

    public String getState() {
        return state;
    }
}
