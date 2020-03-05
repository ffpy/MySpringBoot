package com.ganguomob.dev.myspringboot.socketio.model;

import com.corundumstudio.socketio.AckCallback;
import com.corundumstudio.socketio.SocketIOClient;

import java.util.Set;

/**
 * @author wenlongsheng
 * @date 2020/3/5
 */
public interface Client extends SocketIOClient {

    SocketIOClient getRawClient();

    <T> T getUser();

    Long getUserId();

    void sendRawEvent(String name, Object... data);

    void sendRawEvent(String name, AckCallback<?> ackCallback, Object... data);

    void copyProperties(Client source);

    Set<String> keySet();
}
