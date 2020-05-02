package org.ffpy.myspringboot.socketio.model;

import com.corundumstudio.socketio.BroadcastAckCallback;
import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOClient;
import org.ffpy.myspringboot.socketio.service.BaseSocketService;
import lombok.NonNull;
import lombok.experimental.Delegate;

/**
 * @author wenlongsheng
 * @date 2020/3/5
 */
public class MyBroadcastOperations extends BroadcastOperations {

    @NonNull
    @Delegate(excludes = NoDelegate.class)
    private final BroadcastOperations broadcastOperations;

    @NonNull
    private final BaseSocketService socketService;

    public MyBroadcastOperations(BroadcastOperations broadcastOperations,
                                 BaseSocketService socketService) {
        super(null, null);
        this.broadcastOperations = broadcastOperations;
        this.socketService = socketService;
    }

    public BroadcastOperations getRawBroadcastOperations() {
        return broadcastOperations;
    }

    @Override
    public void sendEvent(String name, Object... data) {
        if (data.length == 0) {
            broadcastOperations.sendEvent(name);
        } else {
            broadcastOperations.sendEvent(name, wrapData(data));
        }
    }

    @Override
    public void sendEvent(String name, SocketIOClient excludedClient, Object... data) {
        if (data.length == 0) {
            super.sendEvent(name, excludedClient);
        } else {
            super.sendEvent(name, excludedClient, wrapData(data));
        }
    }

    @Override
    public <T> void sendEvent(String name, Object data, BroadcastAckCallback<T> ackCallback) {
        super.sendEvent(name, socketService.wrapData(data), ackCallback);
    }

    @Override
    public <T> void sendEvent(String name, Object data, SocketIOClient excludedClient, BroadcastAckCallback<T> ackCallback) {
        super.sendEvent(name, socketService.wrapData(data), excludedClient, ackCallback);
    }

    private Object wrapData(Object[] data) {
        if (data.length != 1) {
            throw new IllegalArgumentException("只能传一个参数");
        }
        return socketService.wrapData(data[0]);
    }

    private interface NoDelegate {

        void sendEvent(String name, Object... data);

        void sendEvent(String name, SocketIOClient excludedClient, Object... data);

        <T> void sendEvent(String name, Object data, BroadcastAckCallback<T> ackCallback);

        <T> void sendEvent(String name, Object data, SocketIOClient excludedClient, BroadcastAckCallback<T> ackCallback);
    }
}
