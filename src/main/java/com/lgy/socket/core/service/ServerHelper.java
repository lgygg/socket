package com.lgy.socket.core.service;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.lgy.socket.core.IChannelInitAction;
import com.lgy.socket.core.IService;
import com.lgy.socket.core.SocketBase;
import com.lgy.socket.core.bean.PkgDataBean;
import com.lgy.socket.core.bean.TransferData;
import com.lgy.socket.core.common.BaseChannelInitializer;
import com.lgy.socket.core.common.BoundHandler;
import com.lgy.socket.core.common.CallBack;
import com.lgy.socket.core.common.Constant;
import com.lgy.socket.core.common.TransferAction;

import com.lgy.socket.util.Global;
import com.lgy.socket.util.LogHelper;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServerHelper implements IService, IChannelInitAction, TransferAction {

    private SocketBase socket;
    private Map<String, Channel> mapChannel = new HashMap<>();
    private Channel currentChannel = null;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private CallBack callBack;
    private ChannelInitializer initializer = new ServerChannelInitializer();

    public ServerHelper(SocketBase socket) {
        this.socket = socket;
        call(this.socket.getCurrentState(), null);
    }

    @Override
    public void listen() {
        if (socket.getCurrentState().getLevel() > State.INIT.getLevel()) {
            return;
        }
        socket.setCurrentState(State.START);
        TransferData transferData = new TransferData();
        Map<String, Object> data = new HashMap<>();
        data.put(Constant.DATA, Constant.START_SERVER);
        transferData.setData(data);
        call(this.socket.getCurrentState(), transferData);
        Global.getInstance().getThreadPool().execute(() -> {
            // bossGroup用于接受客户端连接
            bossGroup = new NioEventLoopGroup(1);
            // 用于处理客户端数据读写
            workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();
                InetSocketAddress inetSocketAddress = new InetSocketAddress(socket.getPort());
                b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).localAddress(inetSocketAddress)
                    .childOption(ChannelOption.SO_KEEPALIVE, true).childOption(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.TCP_NODELAY, true).childHandler(this.initializer);

                // Bind and start to accept incoming connections.
                ChannelFuture channelFuture = b.bind().sync();
                LogHelper.d(
                    ServerHelper.class.getName() + " started and listen on " + channelFuture.channel().localAddress());
                socket.setCurrentState(State.CONNECTING);
                TransferData connectingData = new TransferData();
                connectingData.setFromIp(socket.getIp());
                connectingData.setFromPort(socket.getPort());
                Map<String, Object> msg = new HashMap<>();
                msg.put(Constant.DATA, "Started and listen on " + channelFuture.channel().localAddress());
                connectingData.setData(msg);
                call(socket.getCurrentState(), connectingData);
                channelFuture.channel().closeFuture().sync();
            } catch (Exception e) {
                LogHelper.e(e.getLocalizedMessage());
            } finally {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
                socket.setCurrentState(State.END);
                call(socket.getCurrentState(), null);
            }
        });
    }

    @Override
    public void accept() {

    }

    @Override
    public void close() {
        initializer = null;
    }

    /**
     * 切换通道 设置服务端，与哪个客户端通信
     */
    public void selectorChannel(String ip, Integer port) {
        currentChannel = mapChannel.get(ip + ":" + port);
    }

    public Map<String, Channel> getChannels() {
        return mapChannel;
    }

    public void setCallBack(CallBack<TransferData> callBack) {
        this.callBack = callBack;
    }

    private void call(State state, TransferData t) {
        if (callBack != null) {
            callBack.onState(state, t);
        }
    }

    @Override
    public void setChannelInitializer(BaseChannelInitializer initializer) {
        initializer.setHandler(new BoundHandler<PkgDataBean<String>>() {
            @Override
            public void read(String ip, Integer port, PkgDataBean<String> stringPkgDataBean) {
                TransferData transferData = new TransferData();
                transferData.setFromIp(ip);
                transferData.setFromPort(port);
                Map<String, Object> data = new HashMap<>();
                data.put(Constant.DATA, stringPkgDataBean.getData());
                transferData.setData(data);
                call(State.CONNECTED, transferData);
            }

            @Override
            public void channelActive(String ip, Integer port, Channel channel) {
                // 客户端连接的时候触发
                TransferData transferData = new TransferData();
                transferData.setFromIp(ip);
                transferData.setFromPort(port);
                Map<String, Object> data = new HashMap<>();
                data.put(Constant.DATA, Constant.SERVER_ACCEPT_CONNECT);
                transferData.setData(data);
                call(State.CONNECTED, transferData);
                if (channel != null && channel.remoteAddress() != null) {
                    mapChannel.put(ip + ":" + port, channel);
                }
            }

            @Override
            public void channelInactive(String ip, Integer port, Channel channel) {
                // 客户端断开连接的时候触发
                TransferData transferData = new TransferData();
                transferData.setFromIp(ip);
                transferData.setFromPort(port);
                Map<String, Object> data = new HashMap<>();
                data.put(Constant.DATA, Constant.SERVER_DISCONNECT);
                transferData.setData(data);
                call(State.DISCONNECTED, transferData);
                if (channel != null && channel.remoteAddress() != null) {
                    mapChannel.remove(ip + ":" + port);
                }
            }
        });
        this.initializer = initializer;
    }

    @Override
    public <T> void send(T data) throws Exception {
        if (data instanceof String) {
            PkgDataBean<String> bean = new PkgDataBean();
            bean.setCmd(TransferType.STRING_TYPE.getKey());
            bean.setData((String)data);
            bean.setDataLength((byte) bean.getData().getBytes().length);
            if (currentChannel != null && currentChannel.isActive()) {
                currentChannel.writeAndFlush(bean);
            }
        } else {
            throw new Exception("no support this type");
        }

    }
}
