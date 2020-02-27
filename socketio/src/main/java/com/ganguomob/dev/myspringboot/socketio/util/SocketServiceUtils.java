package com.ganguomob.dev.myspringboot.socketio.util;

/**
 * @author wenlongsheng
 * @date 2020/2/21
 */
public class SocketServiceUtils {

    /**
     * 给命名空间加上'/'前缀
     */
    public static String getNamespace(String namespace) {
        return namespace.startsWith("/") ? namespace : "/" + namespace;
    }
}
