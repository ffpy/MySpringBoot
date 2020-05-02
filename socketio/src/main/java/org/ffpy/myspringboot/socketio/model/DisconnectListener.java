package org.ffpy.myspringboot.socketio.model;

/**
 * @author wenlongsheng
 * @date 2020/3/5
 */
public interface DisconnectListener {

    void onDisconnect(Client client);
}
