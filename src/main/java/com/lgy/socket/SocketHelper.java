package com.lgy.socket;

import com.lgy.socket.core.bean.TransferData;
import com.lgy.socket.core.client.Client;
import com.lgy.socket.core.client.ClientHelper;
import com.lgy.socket.core.common.CallBack;
import com.lgy.socket.core.service.Server;
import com.lgy.socket.core.service.ServerChannelInitializer2;
import com.lgy.socket.core.service.ServerHelper;

import android.text.TextUtils;

public class SocketHelper {

    private SocketHelper() {}

    private ServerHelper serverHelper;
    private ClientHelper clientHelper;

    private static class SocketHelperInner {
        private static final SocketHelper instance = new SocketHelper();
    }

    public static SocketHelper getInstance() {
        return SocketHelper.SocketHelperInner.instance;
    }

    public void createServer(int port, CallBack<TransferData> callBack) {

        serverHelper = new ServerHelper(new Server.Builder().setPort(port).build());
        serverHelper.setCallBack(callBack);
        serverHelper.setChannelInitializer(new ServerChannelInitializer2());
        serverHelper.listen();
    }

    public void closeServer() {
        if (serverHelper != null) {
            serverHelper.close();
            serverHelper = null;
        }

    }

    public ClientHelper createClient(String ip, int port, CallBack<TransferData> callBack) {
        clientHelper = new ClientHelper(new Client.Builder().setPort(port).setServerIp(ip).build());
        clientHelper.setCallBack(callBack);
        clientHelper.connect();
        return clientHelper;
    }

    public void closeClient() {
        if (clientHelper != null) {
            clientHelper.close();
            clientHelper = null;
        }
    }

    public <T> void serverSend(T data) throws Exception {
        if (serverHelper != null) {
            serverHelper.send(data);
        }
    }

    public void selectClient(String ip, Integer port) throws Exception {
        if (serverHelper != null) {
            serverHelper.selectorChannel(ip, port);
        }
    }

    public void selectClient(String addr) throws Exception {
        if (serverHelper != null) {
            String[] addrs = !TextUtils.isEmpty(addr) ? addr.split(":") : new String[2];
            serverHelper.selectorChannel(addrs[0], Integer.valueOf(addrs[1]));
        }
    }

    public <T> void clientSend(T data) throws Exception {
        if (clientHelper != null) {
            clientHelper.send(data);
        }
    }
}
