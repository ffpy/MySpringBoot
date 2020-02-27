package com.ganguomob.dev.myspringboot.socketio.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.protocol.JacksonJsonSupport;
import com.corundumstudio.socketio.protocol.JsonSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.ganguomob.dev.myspringboot.common.util.SpringContextUtils;
import com.ganguomob.dev.myspringboot.socketio.JsonSupportPropertyNamingStrategy;
import com.ganguomob.dev.myspringboot.socketio.SocketServiceClassPathScanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author wenlongsheng
 * @date 2020/2/20
 */
@Configuration
@Slf4j
public class SocketServiceConfiguration implements DisposableBean {

    private static final String PARAM_TOKEN = "token";

    @Bean("socketIOService")
    public SocketIOServer server(@Autowired(required = false) ObjectMapper objectMapper,
                                 SocketConfig socketConfig) {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(socketConfig.getHostname());
        config.setPort(socketConfig.getPort());
        config.setRandomSession(true);
        if (objectMapper != null) {
            config.setJsonSupport(getJsonSupport(objectMapper));
        }
        // token鉴权
        config.setAuthorizationListener(socketConfig.getAuthorizationListener());

        SocketIOServer server = new SocketIOServer(config);
        SocketServiceClassPathScanner.getNamespaces().forEach(server::addNamespace);

        server.addConnectListener(socketConfig.getConnectListener());

        // debug模式不开启服务
        if (!socketConfig.isDebug()) {
            server.start();
            log.info("创建SocketIOService成功 hostname: {}, port: {}",
                    socketConfig.getHostname(), socketConfig.getPort());
        }

        return server;
    }

    @Override
    public void destroy() throws Exception {
        if (!SpringContextUtils.getBean(SocketConfig.class).isDebug()) {
            SpringContextUtils.getBean(SocketIOServer.class).stop();
            log.info("关闭SocketIOService成功");
        }
    }

    private JsonSupport getJsonSupport(ObjectMapper objectMapper) {
        // 通过反射来使用自定义的ObjectMapper
        objectMapper = objectMapper.copy();
        objectMapper.setPropertyNamingStrategy(
                new JsonSupportPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE));

        JacksonJsonSupport jsonSupport = new JacksonJsonSupport();
        Class<? extends JacksonJsonSupport> cls = JacksonJsonSupport.class;

        Field field;
        try {
            field = cls.getDeclaredField("objectMapper");
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("filed objectMapper not found.", e);
        }

        Method init;
        try {
            init = cls.getDeclaredMethod("init", ObjectMapper.class);
            init.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("method init not found.", e);
        }

        try {
            field.set(jsonSupport, objectMapper);
            init.invoke(jsonSupport, objectMapper);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return jsonSupport;
    }
}
