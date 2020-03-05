package com.ganguomob.dev.myspringboot.socket;

import com.ganguomob.dev.myspringboot.socketio.model.Client;
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
@Slf4j
@SocketService(namespace = "chat")
public class ChatService extends BaseSocketService {

    @Override
    protected void onConnect(Client client) {
        client.sendEvent("hello", "hello, " + client.getRemoteAddress().toString());
        getBroadcastOperations().sendEvent("join", client, client.getRemoteAddress().toString());
    }

    @Override
    protected void onDisconnect(Client client) {
        getBroadcastOperations().sendEvent("leave", client, client.getRemoteAddress().toString());
    }

    @EventHandler
    public void sendMsg(Client client, String msg) {
        log.info("msg from {}: {}", client.getRemoteAddress(), msg);
        getBroadcastOperations().sendEvent("newMsg", new Msg(client.getRemoteAddress().toString(), msg));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Msg {
        private String from;
        private String content;
    }
}
