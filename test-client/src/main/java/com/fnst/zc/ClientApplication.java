package com.fnst.zc;

import com.fnst.zc.api.IHelloService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ClientApplication.class);
        IHelloService helloService = context.getBean(IHelloService.class);
        helloService.sayHello("mayanqing");
    }

}
