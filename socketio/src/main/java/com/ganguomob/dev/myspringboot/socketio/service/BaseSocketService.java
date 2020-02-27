package com.ganguomob.dev.myspringboot.socketio.service;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.ganguomob.dev.myspringboot.socketio.config.SocketIOServerBuilder;
import com.ganguomob.dev.myspringboot.socketio.util.SocketServiceUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

/**
 * @param <U> 用户类型
 * @author wenlongsheng
 * @date 2020/2/21
 */
public abstract class BaseSocketService<U> implements ApplicationContextAware {

    /** 绑定的SocketIOServer */
    private SocketIOServer server;

    /** 绑定的Namespace */
    private SocketIONamespace namespace;

    private SocketUserDetailsService userDetailService;

    /**
     * 在这里执行添加监听器等初始化动作
     */
    protected void init() {
    }

    /**
     * 新用户连接时通知
     */
    protected void onConnect(SocketIOClient client) {
    }

    /**
     * 有连接断开时通知
     */
    protected void onDisconnect(SocketIOClient client) {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        bindServer(applicationContext);
        bindEventListeners();
        getNamespace().addConnectListener(this::onConnect);
        getNamespace().addDisconnectListener(this::onDisconnect);
        init();
    }

    /**
     * 获取绑定的SocketIOServer
     */
    public SocketIOServer getServer() {
        return server;
    }

    /**
     * 获取绑定的Namespace
     */
    public SocketIONamespace getNamespace() {
        return namespace;
    }

    /**
     * 获取当前服务的广播操作
     */
    public BroadcastOperations getBroadcastOperations() {
        return namespace.getBroadcastOperations();
    }

    /**
     * 获取当前服务的所有客户端
     */
    public Collection<SocketIOClient> getAllClients() {
        return namespace.getAllClients();
    }

    /**
     * 获取指定连接的User
     */
    @SuppressWarnings("unchecked")
    protected U getUser(SocketIOClient client) {
        if (userDetailService == null) {
            throw new RuntimeException("找不到userDetailService");
        }
        return (U) userDetailService.getUser(client);
    }

    /**
     * 绑定Server
     */
    private void bindServer(ApplicationContext applicationContext) {
        SocketService annotation = getClass().getAnnotation(SocketService.class);
        String serverName = annotation.server();
        String namespaceName = SocketServiceUtils.getNamespace(annotation.namespace());

        if (StringUtils.isEmpty(serverName)) {
            this.server = applicationContext.getBean(SocketIOServer.class);
        } else {
            this.server = applicationContext.getBean(serverName, SocketIOServer.class);
        }

        if (this.server.getNamespace(namespaceName) != null) {
            throw new RuntimeException(serverName + "的" + namespaceName + "已被绑定");
        }

        this.namespace = this.server.addNamespace(namespaceName);
        this.userDetailService = SocketIOServerBuilder.USER_DETAILS_SERVICE_MAP.get(this.server);
    }

    /**
     * 绑定Event事件到Service的方法
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void bindEventListeners() {
        for (Method method : getClass().getMethods()) {
            EventHandler eventHandler = method.getAnnotation(EventHandler.class);
            if (eventHandler == null) {
                continue;
            }

            String event = eventHandler.value();
            if (StringUtils.isEmpty(event)) {
                event = method.getName();
            }

            // 获取数据类型
            Class dataType = Object.class;
            for (Class<?> type : method.getParameterTypes()) {
                if (type != SocketIOClient.class && type != AckRequest.class) {
                    if (dataType != Object.class) {
                        throw new RuntimeException("方法" + method.getName() + "只能有一个数据参数");
                    }
                    dataType = type;
                }
            }

            // 绑定Event
            namespace.addEventListener(event, dataType, (client, data, ackSender) -> {
                Object[] args = Arrays.stream(method.getParameterTypes())
                        .map(type -> {
                            if (type == SocketIOClient.class) {
                                return client;
                            }
                            if (type == AckRequest.class) {
                                return ackSender;
                            }
                            return data;
                        }).toArray(Object[]::new);

                Object result = method.invoke(this, args);

                if (method.getReturnType() != void.class) {
                    ackSender.sendAckData(result);
                }
            });
        }
    }
}
