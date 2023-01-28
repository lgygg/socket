package com.lgy.socket.core.service;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ServerEncoder extends MessageToByteEncoder<Object> {
    private static final String TAG = "ServerEncoder";
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object data, ByteBuf byteBuf) throws Exception {
        //自己发送过来的东西进行编码
        byteBuf.writeBytes(data.toString().getBytes());
    }
}