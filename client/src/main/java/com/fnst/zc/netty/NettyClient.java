package com.fnst.zc.netty;

import com.fnst.zc.client.Client;
import com.fnst.zc.coder.RpcDecoder;
import com.fnst.zc.coder.RpcEncoder;
import com.fnst.zc.entity.RpcRequest;
import com.fnst.zc.entity.RpcResponse;
import com.fnst.zc.netty.handler.ClientHandler;
import com.fnst.zc.serialization.JsonSerialization;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.net.InetSocketAddress;

public class NettyClient implements Client {

    private EventLoopGroup eventLoopGroup;
    private Channel channel;
    private ClientHandler clientHandler;

    private String host;
    private int port;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public RpcResponse send(RpcRequest rpcRequest) {
        try {
            channel.writeAndFlush(rpcRequest).await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return clientHandler.getRpcResponse(rpcRequest.getRequestId());
    }

    @Override
    public void connect(InetSocketAddress inetSocketAddress) {
        clientHandler = new ClientHandler();
        eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4));
                        pipeline.addLast(new RpcEncoder(RpcRequest.class, new JsonSerialization()));
                        pipeline.addLast(new RpcDecoder(RpcResponse.class, new JsonSerialization()));
                        pipeline.addLast(clientHandler);
                    }
                });

        try {
            channel = bootstrap.connect(inetSocketAddress).sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public InetSocketAddress getInetSocketAddress() {
        return new InetSocketAddress(host, port);
    }

    @Override
    public void close() {
        eventLoopGroup.shutdownGracefully();
        channel.closeFuture().syncUninterruptibly();
    }
}
