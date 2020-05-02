package org.ffpy.myspringboot.socket;

import org.ffpy.myspringboot.socketio.model.Client;
import org.ffpy.myspringboot.socketio.service.BaseSocketService;
import org.ffpy.myspringboot.socketio.service.EventHandler;
import org.ffpy.myspringboot.socketio.service.SocketService;
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
public class TestService extends BaseSocketService {

    @Override
    protected void onConnect(Client client) {
        log.debug("new client: {}, token: {}", client.getRemoteAddress(), client.get("token"));
    }

    @EventHandler("hello")
    public HelloResponse hello(Client client, HelloRequest request) {
        log.debug("hello from {}({}), data: {}", client.getRemoteAddress(), getUser(client), request);
//        throw new CommonException("测试报错");
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
