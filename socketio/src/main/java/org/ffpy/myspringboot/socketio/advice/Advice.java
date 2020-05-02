package org.ffpy.myspringboot.socketio.advice;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;

/**
 * @author wenlongsheng
 * @date 2020/2/28
 */
public interface Advice {

    boolean supports(SocketIOServer server, SocketIONamespace namespace);
}
