package com.lgy.socket.core.client;

import com.lgy.socket.core.IChannelInitAction;
import com.lgy.socket.core.IClient;
import com.lgy.socket.core.SocketBase;
import com.lgy.socket.core.bean.PkgDataBean;
import com.lgy.socket.core.common.BaseChannelInitializer;
import com.lgy.socket.core.common.BoundHandler;
import com.lgy.socket.core.common.CallBack;
import com.lgy.socket.core.common.TransferAction;
import com.lgy.socket.core.service.State;
import com.lgy.socket.core.service.TransferType;
import com.lgy.socket.util.Global;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ClientHelper implements IClient, IChannelInitAction, TransferAction {

    private EventLoopGroup group;
    private Channel channel;
    private ChannelFuture channelFuture;
    private CallBack callBack;
    private SocketBase socket;

    public ClientHelper(SocketBase socket){
        this.socket = socket;
        call(this.socket.getCurrentState(),null);
    }
    @Override
    public void connect() {
        if (this.socket.getCurrentState() == State.CONNECTING || this.socket.getCurrentState() == State.START) {
            return;
        }
        this.socket.setCurrentState(State.START);
        call(State.START, Client.class.getName() + " client start " + this.socket.getIp()+":"+this.socket.getPort());
        Global.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                connectServer();
            }
        });
    }

    @Override
    public void close() {
        group.shutdownGracefully();
    }

    @Override
    public void setChannelInitializer(BaseChannelInitializer<SocketChannel> initializer) {

    }

    public <T> void setCallBack(CallBack<T> callBack) {
        this.callBack = callBack;
    }

    private <T> void call(State state,T t){
        if (callBack != null) {
            callBack.onState(state,t);
        }
    }

    private void connectServer() {
        group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap().group(group)
                .option(ChannelOption.TCP_NODELAY, true)//屏蔽Nagle算法试图
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .channel(NioSocketChannel.class)
                .handler(new ClientChannelInitializer(new BoundHandler<PkgDataBean<String>>() {
                    @Override
                    public void read(String ip, Integer port, PkgDataBean<String> stringPkgDataBean) {
                        call(State.CONNECTED, Client.class.getName() + " connect on " + ip+":"+port);
                    }

                    @Override
                    public void channelActive(String ip, Integer port,Channel channel) {
                        call(State.CONNECTED, Client.class.getName() + " connect on " + ip+":"+port);
                    }

                    @Override
                    public void channelInactive(String ip, Integer port,Channel channel) {
                        call(State.DISCONECTED, Client.class.getName() + " connect on " + ip+":"+port);
                    }
                }));
        // 连接到服务端
         channelFuture = bootstrap.connect(new InetSocketAddress(this.socket.getIp(), this.socket.getPort()));
        //获取连接通道
        channel = channelFuture.channel();
        try {
            this.socket.setCurrentState(State.CONNECTING);
            call(State.CONNECTING, Client.class.getName() + " connect on " + this.socket.getIp()+":"+this.socket.getPort());
            channelFuture.sync().channel();
        } catch (InterruptedException e) {
            this.socket.setCurrentState(State.END);
            call(State.END, Client.class.getName() + " connect fail " + this.socket.getIp()+":"+this.socket.getPort());
            e.printStackTrace();
        }
    }

    @Override
    public <T> void send(T data) throws Exception {
        if (data instanceof String) {
            PkgDataBean<String> bean = new PkgDataBean();
            bean.setCmd(TransferType.STRING_TYPE.getKey());
            bean.setData((String)data);
            bean.setDataLength((byte) bean.getData().getBytes().length);
            if (channelFuture != null && channelFuture.channel().isActive()) {
                channelFuture.channel().writeAndFlush(bean);
            }
        }else{
            throw new Exception("no support this type");
        }

    }
}
