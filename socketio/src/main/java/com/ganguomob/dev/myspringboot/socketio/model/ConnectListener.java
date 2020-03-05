package com.ganguomob.dev.myspringboot.socketio.model;

/**
 * @author wenlongsheng
 * @date 2020/3/5
 */
public interface ConnectListener {

    void onConnect(Client client);
}
