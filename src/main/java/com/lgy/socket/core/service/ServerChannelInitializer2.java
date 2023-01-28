package com.lgy.socket.core.service;

import com.lgy.socket.core.bean.PkgDataBean;
import com.lgy.socket.core.common.BaseChannelInitializer;
import com.lgy.socket.core.common.BoundHandler;
import com.lgy.socket.core.common.StringDecoder;
import com.lgy.socket.core.common.StringEncoder;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class ServerChannelInitializer2 extends BaseChannelInitializer<PkgDataBean<String>> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //添加发送数据编码器
        pipeline.addLast(new StringEncoder());
        //添加解码器，对收到的数据进行解码
        pipeline.addLast(new StringDecoder());
        //添加数据处理
        pipeline.addLast(new ServerStringHandler(this.handler));

    }


}