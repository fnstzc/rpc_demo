package com.fnst.zc.config;

import com.fnst.zc.annotation.RpcInterface;
import com.fnst.zc.proxy.ProxyFactory;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import java.util.Set;

@Configuration
@Slf4j
public class RpcConfig implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() {
        Reflections reflections = new Reflections("com.fnst.zc");
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(RpcInterface.class);
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        for (Class<?> annotatedClass : typesAnnotatedWith) {
            beanFactory.registerSingleton(annotatedClass.getSimpleName(), ProxyFactory.create(annotatedClass));
        }
        log.info("afterPropertiesSet is {}", typesAnnotatedWith);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
