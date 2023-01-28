package com.lgy.socket.core.service;

import com.lgy.socket.core.bean.PkgDataBean;
import com.lgy.socket.util.LogHelper;

import java.util.Arrays;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class ServerStringDecoder extends ByteToMessageDecoder {
    private static final String TAG = "ServerDecoder";

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int length = byteBuf.readableBytes();
        //收到的数据包
        byte[] data = byteBuf.readBytes(length).array();
        //判断数据包是不是一个正确的数据包
        if (data[0] == TransferType.CLIENT_HEART_HIT.getKey() && data[0] == data[data.length - 1]) {
            PkgDataBean bean = new PkgDataBean();
            bean.setCmd(data[1]);
            bean.setDataLength(data[2]);
            byte[] bytes = Arrays.copyOfRange(data, 3, 3 + bean.getDataLength());
            bean.setData(new String(bytes));
//            LogHelper.d("收到了客户端发送的数据：" + bean.toString());
        }
    }
}
