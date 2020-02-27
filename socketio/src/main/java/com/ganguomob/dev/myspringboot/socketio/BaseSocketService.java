package com.ganguomob.dev.myspringboot.socketio;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 * @author wenlongsheng
 * @date 2020/2/21
 */
public abstract class BaseSocketService implements InitializingBean {
    public static final String USER = "user";

    @Autowired
    protected SocketIOServer server;

    private SocketIONamespace namespace;

    /**
     * 在这里执行添加监听器等动作
     */
    protected abstract void init();

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    /**
     * 用于在扫描注解的时候自动注入
     */
    public void setNamespace(SocketIONamespace namespace) {
        this.namespace = namespace;
    }

    /**
     * 获取当前服务的命名空间
     */
    public SocketIONamespace getNamespace() {
        return this.namespace;
    }

    /**
     * 获取当前服务的广播操作
     */
    public BroadcastOperations getBroadcastOperations() {
        return this.namespace.getBroadcastOperations();
    }

    /**
     * 获取当前服务的所有客户端
     */
    public Collection<SocketIOClient> getAllClients() {
        return getNamespace().getAllClients();
    }

    /**
     * 获取指定客户端的用户ID
     *
     * @param client 客户端
     * @return 用户ID
     */
    public Long getUserId(SocketIOClient client) {
        return client.get(USER);
    }
}
