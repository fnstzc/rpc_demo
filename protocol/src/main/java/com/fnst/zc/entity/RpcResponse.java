package com.fnst.zc.entity;

import lombok.Data;

@Data
public class RpcResponse {

    private String requestId;
    private Throwable throwable;
    private Object result;

}
