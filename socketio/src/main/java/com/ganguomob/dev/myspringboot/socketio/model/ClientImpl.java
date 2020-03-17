package com.ganguomob.dev.myspringboot.socketio.model;

import com.corundumstudio.socketio.AckCallback;
import com.corundumstudio.socketio.SocketIOClient;
import com.ganguomob.dev.myspringboot.socketio.service.BaseSocketService;
import com.ganguomob.dev.myspringboot.socketio.service.SocketUserDetailsService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author wenlongsheng
 * @date 2020/3/5
 */
@RequiredArgsConstructor
public class ClientImpl implements Client {

    private static final String KEY_USER_ID = "__user_id__";

    @NonNull
    @Delegate(types = SocketIOClient.class, excludes = NoDelegate.class)
    private final SocketIOClient client;

    @NonNull
    private final SocketUserDetailsService userDetailsService;

    private final Set<String> keySet = new HashSet<>();

    @NonNull
    private final BaseSocketService socketService;

    @Override
    public SocketIOClient getRawClient() {
        return client;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getUser() {
        return (T) userDetailsService.getUser(client);
    }

    @Override
    public Long getUserId() {
        Long userId = client.get(KEY_USER_ID);
        if (userId == null) {
            userId = userDetailsService.getUserId(client);
            client.set(KEY_USER_ID, userId);
        }
        return userId;
    }

    @Override
    public void sendEvent(String name, Object... data) {
        if (data.length == 0) {
            client.sendEvent(name);
        } else {
            client.sendEvent(name, wrapData(data));
        }
    }

    @Override
    public void sendEvent(String name, AckCallback<?> ackCallback, Object... data) {
        if (data.length == 0) {
            client.sendEvent(name);
        } else {
            client.sendEvent(name, ackCallback, wrapData(data));
        }
    }

    @Override
    public void sendRawEvent(String name, Object... data) {
        client.sendEvent(name, data);
    }

    @Override
    public void sendRawEvent(String name, AckCallback<?> ackCallback, Object... data) {
        client.sendEvent(name, ackCallback, data);
    }

    @Override
    public void copyProperties(Client source) {
        for (String key : source.keySet()) {
            set(key, source.get(key));
        }
    }

    @Override
    public void set(String key, Object val) {
        keySet.add(key);
        this.client.set(key, val);
    }

    @Override
    public void del(String key) {
        keySet.remove(key);
        this.client.del(key);
    }

    @Override
    public Set<String> keySet() {
        return Collections.unmodifiableSet(keySet);
    }

    private Object wrapData(Object[] data) {
        if (data.length != 1) {
            throw new IllegalArgumentException("只能传一个参数");
        }
        return socketService.wrapData(data[0]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientImpl client1 = (ClientImpl) o;
        return Objects.equals(client, client1.client);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client);
    }

    private interface NoDelegate {

        void sendEvent(String name, Object... data);

        void sendEvent(String name, AckCallback<?> ackCallback, Object... data);

        void set(String key, Object val);

        void del(String key);
    }
}
