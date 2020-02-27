package com.ganguomob.dev.myspringboot.socketio.config;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.listener.ConnectListener;
import lombok.Data;

/**
 * @author wenlongsheng
 * @date 2020/2/27
 */
@Data
public class SocketConfig {

    /** 绑定的地址 */
    private String hostname;

    /** 监听的端口 */
    private int port;

    /** 是否开启调试模式 */
    private boolean debug;

    private AuthorizationListener authorizationListener;

    private ConnectListener connectListener;
}
