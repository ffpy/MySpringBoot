package com.ganguomob.dev.myspringboot.socketio.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.ganguomob.dev.myspringboot.socketio.auth.Authentication;
import com.ganguomob.dev.myspringboot.socketio.service.SocketUserDetailsService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wenlongsheng
 * @date 2020/2/27
 */
public class SocketIOServerBuilder {
    private static final Map<SocketIOServer, SocketUserDetailsService> userDetailsServiceMap = new HashMap<>();

    private SocketIOConfigBuilder config;
    private SocketUserDetailsService userDetailService;

    public static Map<SocketIOServer, SocketUserDetailsService> getUserDetailsServiceMap() {
        return Collections.unmodifiableMap(userDetailsServiceMap);
    }

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
            config.setAuthorizationListener(data -> {
                Authentication authentication = userDetailService.loadAuthentication(data);
                return authentication != null && userDetailService.verifyAuthentication(authentication);
            });
        }

        SocketIOServer server = new SocketIOServer(config.build());

        if (userDetailService != null) {
            userDetailsServiceMap.put(server, userDetailService);

            server.addConnectListener(client -> {
                Authentication authentication = userDetailService.loadAuthentication(client.getHandshakeData());
                if (authentication == null || !userDetailService.verifyAuthentication(authentication)) {
                    client.disconnect();
                } else {
                    userDetailService.setAuthentication(client, authentication);
                }
            });
        }

        return server;
    }

}
