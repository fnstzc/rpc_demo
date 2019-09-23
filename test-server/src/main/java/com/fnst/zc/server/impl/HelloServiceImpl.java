package com.fnst.zc.server.impl;

import com.fnst.zc.api.IHelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HelloServiceImpl implements IHelloService {
    @Override
    public String sayHello(String name) {
        log.info(name);
        return "Hello" + name;
    }
}
