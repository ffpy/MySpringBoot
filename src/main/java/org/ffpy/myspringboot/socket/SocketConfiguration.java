package org.ffpy.myspringboot.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ffpy.myspringboot.socketio.config.SocketIOConfigBuilder;
import org.ffpy.myspringboot.socketio.config.SocketIOServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wenlongsheng
 * @date 2020/2/27
 */
@Configuration
public class SocketConfiguration {

    @Value("${socketio.hostname:0.0.0.0}")
    private String hostname;

    @Value("${socketio.port}")
    private int port;

    @Bean("socketIOService")
    public SocketIOServer server(@Autowired(required = false) ObjectMapper objectMapper,
                                 SocketUserDetailsServiceImpl userDetailService) {
        return new SocketIOServerBuilder(new SocketIOConfigBuilder()
                .setHostname(hostname)
                .setPort(port)
                .setObjectMapper(objectMapper))
                .setUserDetailService(userDetailService)
                .build();
    }
}
