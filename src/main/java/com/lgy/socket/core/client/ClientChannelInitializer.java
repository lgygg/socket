package com.lgy.socket.core.client;

import com.lgy.socket.core.bean.PkgDataBean;
import com.lgy.socket.core.common.BaseChannelInitializer;
import com.lgy.socket.core.common.BoundHandler;
import com.lgy.socket.core.common.StringDecoder;
import com.lgy.socket.core.common.StringEncoder;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

class ClientChannelInitializer extends BaseChannelInitializer<PkgDataBean<String>> {

    public ClientChannelInitializer(BoundHandler<PkgDataBean<String>> handler){
        super();
        this.handler = handler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //添加心跳处理
        pipeline.addLast(new IdleStateHandler(10,0,0));
        //添加发送数据编码器
        pipeline.addLast(new StringEncoder());
        //对收到数据解码
        pipeline.addLast(new StringDecoder());
        //添加数据处理器
        pipeline.addLast(new ClientStringHandler(this.handler));

    }
}