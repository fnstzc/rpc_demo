package com.fnst.zc.api;

import com.fnst.zc.annotation.RpcInterface;

@RpcInterface
public interface IHelloService {

    String sayHello(String name);

}
