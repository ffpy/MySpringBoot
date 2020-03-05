package com.ganguomob.dev.myspringboot.socketio.model;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.listener.DataListener;

/**
 * @author wenlongsheng
 * @date 2020/3/5
 */
public interface Namespace extends SocketIONamespace {

    SocketIONamespace getRawNamespace();

    <T> void addRawEventListener(String eventName, Class<T> eventClass, DataListener<T> listener);

    void addConnectListener(ConnectListener listener);

    void addDisconnectListener(DisconnectListener listener);

    MyBroadcastOperations getBroadcastOperations();

    MyBroadcastOperations getRoomOperations(String room);
}
