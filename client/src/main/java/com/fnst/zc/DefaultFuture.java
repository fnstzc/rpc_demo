package com.fnst.zc;

import com.fnst.zc.entity.RpcResponse;

public class DefaultFuture {

    private RpcResponse rpcResponse;
    private volatile boolean isSuccess = false;
    private final Object object = new Object();

    public RpcResponse getRpcResponse(int timeout) {
        synchronized (object) {
            while (!isSuccess) {
                try {
                    object.wait(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return rpcResponse;
        }
    }

    public void setRpcResponse(RpcResponse rpcResponse) {
        if (isSuccess) {
            return;
        }
        synchronized (object) {
            this.rpcResponse = rpcResponse;
            this.isSuccess = true;
            object.notify();
        }
    }

}
