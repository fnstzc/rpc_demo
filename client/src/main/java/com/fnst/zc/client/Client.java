package com.fnst.zc.client;

import com.fnst.zc.entity.RpcRequest;
import com.fnst.zc.entity.RpcResponse;

import java.net.InetSocketAddress;

public interface Client {

    RpcResponse send(RpcRequest rpcRequest);

    void connect(InetSocketAddress inetSocketAddress);

    InetSocketAddress getInetSocketAddress();

    void close();

}
