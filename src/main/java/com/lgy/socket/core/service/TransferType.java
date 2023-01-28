package com.lgy.socket.core.service;

public enum TransferType {
    CLIENT_HEART_HIT((byte) 0x02), //客户端心跳
    SERVER_HEART_HIT((byte) 0x03), //服务端心跳
    STRING_TYPE((byte) 0x04); //字符串类型

    private byte key;

    TransferType(byte key) {
        this.key = key;
    }

    public byte getKey() {
        return key;
    }

}
