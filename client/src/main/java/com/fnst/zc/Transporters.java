package com.fnst.zc;

import com.fnst.zc.entity.RpcRequest;
import com.fnst.zc.entity.RpcResponse;
import com.fnst.zc.netty.NettyClient;

public class Transporters {

    public static RpcResponse send(RpcRequest request) {
        NettyClient nettyClient = new NettyClient("127.0.0.1", 8080);
        nettyClient.connect(nettyClient.getInetSocketAddress());
        RpcResponse send = nettyClient.send(request);
        return send;
    }

}
