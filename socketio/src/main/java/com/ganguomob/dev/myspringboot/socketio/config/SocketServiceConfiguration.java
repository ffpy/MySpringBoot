package com.ganguomob.dev.myspringboot.socketio.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.ganguomob.dev.myspringboot.common.util.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.Lifecycle;
import org.springframework.context.annotation.Configuration;

/**
 * @author wenlongsheng
 * @date 2020/2/20
 */
@Configuration
@Slf4j
public class SocketServiceConfiguration implements ApplicationContextAware, Lifecycle {

    @Value("${socketio.debug:false}")
    private boolean debug;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext.getBeansOfType(SocketIOServer.class).forEach((name, server) -> {
            // debug模式不开启服务
            if (!debug) {
                server.start();

                com.corundumstudio.socketio.Configuration config = server.getConfiguration();
                log.info("创建SocketIOService成功 hostname: {}, port: {}",
                        config.getHostname(), config.getPort());
            }
        });
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
        if (!debug) {
            SpringContextUtils.getApplicationContext().getBeansOfType(SocketIOServer.class).forEach((name, server) -> {
                server.stop();

                com.corundumstudio.socketio.Configuration config = server.getConfiguration();
                log.info("关闭SocketIOService成功 hostname: {}, port: {}",
                        config.getHostname(), config.getPort());
            });
        }
    }

    @Override
    public boolean isRunning() {
        return true;
    }
}
