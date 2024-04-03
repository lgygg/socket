package com.lgy.socket.core.common;

import java.net.SocketAddress;

import com.lgy.socket.core.SocketBase;

import android.text.TextUtils;

/**
 * @author: Administrator
 * @date: 2024/4/3
 */
public class Util {

    public static SocketBase dealAddress(SocketAddress socketAddress) {
        SocketBase socketBase = new SocketBase();
        if (socketAddress != null) {
            String address = socketAddress.toString();
            if (!TextUtils.isEmpty(address)) {
                String[] addr = address.replace("/", "").split(":");
                if (addr.length > 1) {
                    socketBase.setIp(addr[0]);
                    socketBase.setPort(Integer.valueOf(addr[1]));
                }
            }
        }
        return socketBase;
    }
}
