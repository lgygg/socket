package com.lgy.socket.core.service;

public enum State {
    INIT(0,"init"), //εε§ηΆζ
    START(1,"start"),
    CONNECTING(2,"connection"),
    CONNECTED(3,"connected"),
    DISCONECTED(4,"disconected"),
    END(3,"end");

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
