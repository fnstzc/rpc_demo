package com.fnst.zc.handler;


import com.fnst.zc.entity.RpcRequest;
import com.fnst.zc.entity.RpcResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<RpcRequest> implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(rpcRequest.getRequestId());
        try {
            rpcResponse.setResult(handler(rpcRequest));
        } catch (Throwable throwable) {
            rpcResponse.setThrowable(throwable);
            throwable.printStackTrace();
        }
        channelHandlerContext.writeAndFlush(rpcResponse);
    }

    private Object handler(RpcRequest rpcRequest) throws Throwable {
        Class<?> clz = Class.forName(rpcRequest.getClassName());

        Object serviceBean = applicationContext.getBean(clz);

        Class<?> serviceClass = serviceBean.getClass();

        String methodName = rpcRequest.getMethodName();

        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] parameters = rpcRequest.getParameter();

        FastClass fastClass = FastClass.create(serviceClass);
        FastMethod fastMethod = fastClass.getMethod(methodName, parameterTypes);

        return fastMethod.invoke(serviceBean, parameters);
    }
}
