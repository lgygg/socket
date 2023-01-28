package com.lgy.socket.core.service;

import com.lgy.socket.core.SocketBase;

public class Server extends SocketBase {

    public Server(String ip,int port) {
        super(ip,port);
    }

    public static class Builder{
        int port = -1;
        public Server build(){
            return new Server(null,port);
        }

        public Builder setPort(int port){
            this.port = port;
            return this;
        }
    }
}
