package com.lgy.socket.core.service;

import com.lgy.socket.core.IChannelInitAction;
import com.lgy.socket.core.IService;
import com.lgy.socket.core.SocketBase;
import com.lgy.socket.core.bean.PkgDataBean;
import com.lgy.socket.core.client.Client;
import com.lgy.socket.core.common.BaseChannelInitializer;
import com.lgy.socket.core.common.BoundHandler;
import com.lgy.socket.core.common.CallBack;
import com.lgy.socket.core.common.TransferAction;
import com.lgy.socket.util.Global;
import com.lgy.socket.util.LogHelper;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServerHelper implements IService, IChannelInitAction, TransferAction {

    private SocketBase socket;
    private Map<String,Channel> mapChannel = new HashMap<>();
    private Channel currentChannel = null;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private CallBack callBack;
    private ChannelInitializer initializer = new ServerChannelInitializer();

    public ServerHelper(SocketBase socket){
        this.socket = socket;
        call(this.socket.getCurrentState(),null);
    }
    @Override
    public void listen() {
        if (socket.getCurrentState().getLevel()>State.INIT.getLevel()) {
            return;
        }
        socket.setCurrentState(State.START);
        call(this.socket.getCurrentState(),ServerHelper.class.getName() + " started " + socket.getPort());
        Global.getInstance().getThreadPool().execute(() -> {
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();
                InetSocketAddress inetSocketAddress = new InetSocketAddress(socket.getPort());
                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .localAddress(inetSocketAddress)
                        .childOption(ChannelOption.SO_KEEPALIVE, true)
                        .childOption(ChannelOption.SO_REUSEADDR, true)
                        .childOption(ChannelOption.TCP_NODELAY, true)
                        .childHandler(this.initializer);

                // Bind and start to accept incoming connections.
                ChannelFuture channelFuture = b.bind().sync();
                LogHelper.d(ServerHelper.class.getName() + " started and listen on " + channelFuture.channel().localAddress());
                socket.setCurrentState(State.CONNECTING);
                call(socket.getCurrentState(),ServerHelper.class.getName() + " started and listen on " + channelFuture.channel().localAddress());
                channelFuture.channel().closeFuture().sync();
            } catch (Exception e) {
                LogHelper.e(e.getLocalizedMessage());
            } finally {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
                socket.setCurrentState(State.END);
                call(socket.getCurrentState(),null);

            };
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
     * 切换通道
     * 设置服务端，与哪个客户端通信
     */
    public void selectorChannel(String ip, Integer port) {
        currentChannel = mapChannel.get(ip+":"+port);
    }

    public <T> void setCallBack(CallBack<T> callBack) {
        this.callBack = callBack;
    }

    private <T> void call(State state,T t){
        if (callBack != null) {
            callBack.onState(state,t);
        }
    }

    @Override
    public void setChannelInitializer(BaseChannelInitializer initializer) {
        initializer.setHandler(new BoundHandler<PkgDataBean<String>>() {
            @Override
            public void read(String ip, Integer port, PkgDataBean<String> stringPkgDataBean) {
                call(State.CONNECTED, Client.class.getName() + " connect on " + ip+":"+port);
            }

            @Override
            public void channelActive(String ip, Integer port, Channel channel) {
                call(State.CONNECTED, Client.class.getName() + " connect on " + ip+":"+port);
                if (channel !=null && channel.remoteAddress()!=null) {
                    mapChannel.put(ip+":"+port,channel);
                }
            }

            @Override
            public void channelInactive(String ip, Integer port, Channel channel) {
                call(State.DISCONECTED, Client.class.getName() + " connect on " + ip+":"+port);
                if (channel !=null && channel.remoteAddress()!=null) {
                    mapChannel.remove(ip+":"+port);
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
        }else{
            throw new Exception("no support this type");
        }

    }
}
