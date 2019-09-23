package com.fnst.zc.netty.handler;

import com.fnst.zc.DefaultFuture;
import com.fnst.zc.entity.RpcRequest;
import com.fnst.zc.entity.RpcResponse;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler extends ChannelDuplexHandler {

    private final Map<String, DefaultFuture> futureMap = new ConcurrentHashMap<>();

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof RpcRequest) {
            RpcRequest rpcRequest = (RpcRequest)msg;
            futureMap.put(rpcRequest.getRequestId(), new DefaultFuture());
        }
        super.write(ctx, msg, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RpcResponse) {
            RpcResponse rpcResponse = (RpcResponse) msg;
            DefaultFuture defaultFuture = futureMap.get(rpcResponse.getRequestId());
            defaultFuture.setRpcResponse(rpcResponse);
        }
        super.channelRead(ctx, msg);
    }

    public RpcResponse getRpcResponse(String requestId) {
        try {
            DefaultFuture defaultFuture = futureMap.get(requestId);
            return defaultFuture.getRpcResponse(10);
        } finally {
            futureMap.remove(requestId);
        }
    }
}
