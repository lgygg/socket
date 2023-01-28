package com.lgy.socket.core.client;

import com.lgy.socket.core.SocketBase;

public class Client extends SocketBase {

    public Client(String ip, int port) {
        super(ip, port);

    }

    public static class Builder{
        int port = -1;
        String serverIp;
        public Client build(){
            return new Client(serverIp,port);
        }

        public Client.Builder setPort(int port){
            this.port = port;
            return this;
        }

        public Client.Builder setServerIp(String ip){
            this.serverIp = ip;
            return this;
        }
    }
}