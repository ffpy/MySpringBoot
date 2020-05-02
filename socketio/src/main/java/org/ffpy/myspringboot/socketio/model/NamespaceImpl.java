package org.ffpy.myspringboot.socketio.model;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.listener.DataListener;
import org.ffpy.myspringboot.socketio.service.BaseSocketService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Delegate;

/**
 * @author wenlongsheng
 * @date 2020/3/5
 */
@AllArgsConstructor
public class NamespaceImpl implements Namespace {

    @NonNull
    @Delegate(types = SocketIONamespace.class, excludes = NoDelegate.class)
    private final SocketIONamespace namespace;

    @NonNull
    private BaseSocketService socketService;

    @Override
    public SocketIONamespace getRawNamespace() {
        return namespace;
    }

    @Override
    public <T> void addEventListener(String eventName, Class<T> eventClass, DataListener<T> listener) {
        namespace.addEventListener(eventName, eventClass, socketService.wrapDataListener(listener));
    }

    @Override
    public void addConnectListener(ConnectListener listener) {
        namespace.addConnectListener(client -> listener.onConnect(socketService.getClient(client)));
    }

    @Override
    public void addDisconnectListener(DisconnectListener listener) {
        namespace.addDisconnectListener(client -> listener.onDisconnect(socketService.getClient(client)));
    }

    @Override
    public <T> void addRawEventListener(String eventName, Class<T> eventClass, DataListener<T> listener) {
        namespace.addEventListener(eventName, eventClass, listener);
    }

    @Override
    public MyBroadcastOperations getBroadcastOperations() {
        return new MyBroadcastOperations(namespace.getBroadcastOperations(), socketService);
    }

    @Override
    public MyBroadcastOperations getRoomOperations(String room) {
        return new MyBroadcastOperations(namespace.getRoomOperations(room), socketService);
    }

    private interface NoDelegate {

        <T> void addEventListener(String eventName, Class<T> eventClass, DataListener<T> listener);

        MyBroadcastOperations getBroadcastOperations();

        MyBroadcastOperations getRoomOperations(String room);
    }
}
