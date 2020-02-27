package com.ganguomob.dev.myspringboot.socketio.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.ganguomob.dev.myspringboot.socketio.service.SocketUserDetailsService;
import com.ganguomob.dev.myspringboot.socketio.auth.Authentication;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wenlongsheng
 * @date 2020/2/27
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SocketIOServerBuilder {
    public static final Map<SocketIOServer, SocketUserDetailsService> USER_DETAILS_SERVICE_MAP = new HashMap<>();

    private SocketIOConfigBuilder config;
    private SocketUserDetailsService userDetailService;

    public SocketIOServerBuilder(SocketIOConfigBuilder config) {
        this.config = config;
    }

    public SocketIOConfigBuilder getConfig() {
        return config;
    }

    public SocketIOServerBuilder setUserDetailService(SocketUserDetailsService userDetailService) {
        this.userDetailService = userDetailService;
        return this;
    }

    public SocketUserDetailsService getUserDetailService() {
        return userDetailService;
    }

    public SocketIOServer build() {
        if (userDetailService != null) {
            config.setAuthorizationListener(data -> userDetailService.loadAuthentication(data) != null);
        }

        SocketIOServer server = new SocketIOServer(config.build());

        if (userDetailService != null) {
            USER_DETAILS_SERVICE_MAP.put(server, userDetailService);

            server.addConnectListener(client -> {
                Authentication authentication = (Authentication) userDetailService.loadAuthentication(
                        client.getHandshakeData());
                if (authentication == null) {
                    client.disconnect();
                } else {
                    userDetailService.setAuthentication(client, authentication);
                }
            });
        }

        return server;
    }
}
