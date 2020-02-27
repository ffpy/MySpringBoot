package com.ganguomob.dev.myspringboot.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.ganguomob.dev.myspringboot.socketio.service.BaseSocketService;
import com.ganguomob.dev.myspringboot.socketio.service.EventHandler;
import com.ganguomob.dev.myspringboot.socketio.service.SocketService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wenlongsheng
 * @date 2020/2/27
 */
@SocketService(server = "socketIOService", namespace = "test")
@Slf4j
public class TestService extends BaseSocketService<String> {
    @Override
    protected void init() {
        getNamespace().addConnectListener(client -> {
            log.debug("new client: {}, token: {}", client.getRemoteAddress(), client.get("token"));
        });
    }

    @EventHandler("hello")
    public HelloResponse hello(SocketIOClient client, HelloRequest request) {
        log.debug("hello from {}({}), data: {}", client.getRemoteAddress(), getUser(client), request);
        return new HelloResponse("hello, " + request.getName());
    }

    @Data
    public static class HelloRequest {
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HelloResponse {
        private String msg;
    }
}
