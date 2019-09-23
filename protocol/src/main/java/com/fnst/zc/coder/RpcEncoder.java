package com.fnst.zc.coder;

import com.fnst.zc.api.Serialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> clz;
    private Serialization serialization;

    public RpcEncoder(Class<?> clz, Serialization serialization) {
        this.clz = clz;
        this.serialization = serialization;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf byteBuf) throws Exception {
        if (clz != null) {
            byte[] bytes = serialization.serialize(msg);
            byteBuf.writeInt(bytes.length);
            byteBuf.writeBytes(bytes);
        }
    }
}
