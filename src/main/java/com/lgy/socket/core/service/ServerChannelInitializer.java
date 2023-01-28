package com.lgy.socket.core.service;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    //    private NettyServerListener<String> mListener;
    private String webSocketPath = "/lgy";

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
//        pipeline.addLast("active",new ChannelActiveHandler(mListener));
//        pipeline.addLast("socketChoose",new SocketChooseHandler(webSocketPath));
//
//        pipeline.addLast("string_encoder",new StringEncoder(CharsetUtil.UTF_8));
//        pipeline.addLast("linebased",LineBasedFrameDecoder(1024));
//        pipeline.addLast("string_decoder",StringDecoder(CharsetUtil.UTF_8));
//        pipeline.addLast("commonhandler", CustomerServerHandler(mListener));
        //添加发送数据编码器
        pipeline.addLast(new ServerEncoder());
        //添加解码器，对收到的数据进行解码
        pipeline.addLast(new ServerDecoder());
        //添加数据处理
        pipeline.addLast(new ServerHandler());

    }
}