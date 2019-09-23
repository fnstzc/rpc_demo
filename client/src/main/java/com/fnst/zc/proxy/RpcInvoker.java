package com.fnst.zc.proxy;

import com.fnst.zc.Transporters;
import com.fnst.zc.entity.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.UUID;

public class RpcInvoker<T> implements InvocationHandler {

    private Class<T> clz;

    public RpcInvoker(Class<T> clz) {
        this.clz = clz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = new RpcRequest();

        String requestId = UUID.randomUUID().toString();

        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();

        rpcRequest.setRequestId(requestId);
        rpcRequest.setClassName(className);
        rpcRequest.setMethodName(methodName);
        rpcRequest.setParameterTypes(parameterTypes);
        rpcRequest.setParameter(args);

        return Transporters.send(rpcRequest).getResult();
    }
}
