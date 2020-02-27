package com.ganguomob.dev.myspringboot.socket;

import com.corundumstudio.socketio.HandshakeData;
import com.ganguomob.dev.myspringboot.socketio.config.SocketConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wenlongsheng
 * @date 2020/2/27
 */
@Configuration
public class SocketConfiguration {

    @Value("${socket.hostname:127.0.0.1}")
    private String hostname;

    @Value("${socket.port}")
    private int port;

    @Value("${socket.debug:false}")
    private boolean debug;

    @Bean
    public SocketConfig socketIOConfig() {
        SocketConfig config = new SocketConfig();
        config.setHostname(hostname);
        config.setPort(port);
        config.setDebug(debug);
        config.setAuthorizationListener(data -> StringUtils.isNotEmpty(getToken(data)));
        config.setConnectListener(client -> {
            String token = getToken(client.getHandshakeData());
            if (StringUtils.isEmpty(token)) {
                client.disconnect();
            }
            client.set("token", token);
        });
        return config;
    }

    private String getToken(HandshakeData data) {
        return data.getSingleUrlParam("token");
    }
}
