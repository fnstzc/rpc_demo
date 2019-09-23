package com.fnst.zc;


import com.fnst.zc.server.RpcServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ServerTest {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(ServerTest.class);

        RpcServer rpcServer = context.getBean(RpcServer.class);
        rpcServer.start();
    }

}

