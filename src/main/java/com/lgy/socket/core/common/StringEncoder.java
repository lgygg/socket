package com.lgy.socket.core.common;

import com.lgy.socket.core.bean.PkgDataBean;
import com.lgy.socket.core.service.TransferType;
import com.lgy.socket.util.ByteUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class StringEncoder extends MessageToByteEncoder<PkgDataBean<String>> {
    @Override
    protected void encode(ChannelHandlerContext ctx, PkgDataBean<String> msg, ByteBuf out) throws Exception {
        //根据数据包协议，生成byte数组
        byte[] bytes = {TransferType.STRING_TYPE.getKey(), msg.getCmd(), msg.getDataLength()};
        byte[] dataBytes = msg.getData().getBytes();
        //将所有数据合并成一个byte数组
        byte[] all = ByteUtil.byteMergerAll(bytes, dataBytes, new byte[]{TransferType.STRING_TYPE.getKey()});
        //发送数据
        out.writeBytes(all);
    }
}
