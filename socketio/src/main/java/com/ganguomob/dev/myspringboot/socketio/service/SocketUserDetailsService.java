package com.ganguomob.dev.myspringboot.socketio.service;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.ganguomob.dev.myspringboot.socketio.auth.Authentication;

/**
 * @author wenlongsheng
 * @date 2020/2/27
 */
public interface SocketUserDetailsService {

    /**
     * 从握手请求中获取Authentication
     */
    Authentication loadAuthentication(HandshakeData data);

    /**
     * 存储Authentication到client中
     */
    void setAuthentication(SocketIOClient client, Authentication authentication);

    /**
     * 获取client中的Authentication
     */
    Authentication getAuthentication(SocketIOClient client);

    /**
     * 获取client关联的User
     */
    Object getUser(SocketIOClient client);
}
