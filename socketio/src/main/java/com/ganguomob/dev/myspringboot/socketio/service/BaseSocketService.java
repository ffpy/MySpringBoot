package com.ganguomob.dev.myspringboot.socketio.service;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.ganguomob.dev.myspringboot.socketio.advice.ExceptionHandler;
import com.ganguomob.dev.myspringboot.socketio.advice.ResponseAdvice;
import com.ganguomob.dev.myspringboot.socketio.config.SocketIOServerBuilder;
import com.ganguomob.dev.myspringboot.socketio.model.Client;
import com.ganguomob.dev.myspringboot.socketio.model.ClientImpl;
import com.ganguomob.dev.myspringboot.socketio.model.MyBroadcastOperations;
import com.ganguomob.dev.myspringboot.socketio.model.Namespace;
import com.ganguomob.dev.myspringboot.socketio.model.NamespaceImpl;
import com.ganguomob.dev.myspringboot.socketio.util.AfterAckAction;
import com.ganguomob.dev.myspringboot.socketio.util.SocketServiceUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.xml.ws.Holder;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author wenlongsheng
 * @date 2020/2/21
 */
public abstract class BaseSocketService implements ApplicationContextAware {

    private static final String KEY_CLIENT = "__client__";

    /** 绑定的SocketIOServer */
    private SocketIOServer server;

    /** 绑定的Namespace */
    private Namespace namespace;

    private SocketUserDetailsService userDetailService;

    private ResponseAdvice responseAdvice;

    @SuppressWarnings("rawtypes")
    private List<ExceptionHandler> exceptionHandlers;

    private ApplicationContext applicationContext;

    /**
     * 在这里执行添加监听器等初始化动作
     */
    protected void init() {
    }

    /**
     * 新用户连接时通知
     */
    protected void onConnect(Client client) {
    }

    /**
     * 有连接断开时通知
     */
    protected void onDisconnect(Client client) {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        bindServer();
        bindAdvices();
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
    public Namespace getNamespace() {
        return namespace;
    }

    /**
     * 获取当前服务的广播操作
     */
    public MyBroadcastOperations getBroadcastOperations() {
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
    public <T> Optional<T> getUser(SocketIOClient client) {
        if (userDetailService == null) {
            throw new RuntimeException("找不到userDetailService");
        }
        return Optional.ofNullable((T) userDetailService.getUser(client));
    }

    /**
     * 让数据能通过Advice处理
     *
     * @param data 数据
     * @return 处理后的数据
     */
    public Object wrapData(Object data) {
        return responseAdvice.beforeBodyWrite(data, null);
    }

    /**
     * 在执行addEventListener方法前包装一下DataListener，使Advice生效
     */
    public <T> DataListener<T> wrapDataListener(DataListener<T> listener) {
        return new MyDataListener<>(listener);
    }

    /**
     * 绑定Server
     */
    private void bindServer() {
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

        this.namespace = new NamespaceImpl(this.server.addNamespace(namespaceName), this);
        this.userDetailService = SocketIOServerBuilder.getUserDetailsServiceMap().get(this.server);
    }

    /**
     * 绑定Advice
     */
    private void bindAdvices() {
        responseAdvice = applicationContext.getBeansOfType(ResponseAdvice.class).values().stream()
                .filter(advice -> advice.supports(server, namespace))
                .findFirst()
                .orElse(ResponseAdvice.IDENTITY);

        exceptionHandlers = applicationContext.getBeansOfType(ExceptionHandler.class).values().stream()
                .filter(advice -> advice.supports(server, namespace))
                .collect(Collectors.toList());
    }

    /**
     * 绑定Event事件到Service的方法
     */
    @SuppressWarnings({"rawtypes" , "unchecked"})
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
                if (type != SocketIOClient.class && type != AckRequest.class &&
                        type != Client.class && type != AfterAckAction.class) {
                    if (dataType != Object.class) {
                        throw new RuntimeException("方法" + method.getName() + "只能有一个数据参数");
                    }
                    dataType = type;
                }
            }

            // 绑定Event
            namespace.addEventListener(event, dataType, new MethodDataListener<>(method));
        }
    }

    public Client getClient(SocketIOClient client) {
        ClientImpl c = client.get(KEY_CLIENT);
        if (c == null) {
            c = new ClientImpl(client, userDetailService, this);
            client.set(KEY_CLIENT, c);
        }
        return c;
    }

    private class MyDataListener<T> implements DataListener<T> {

        private DataListener<T> listener;

        public MyDataListener(DataListener<T> listener) {
            this.listener = listener;
        }

        @Override
        public void onData(SocketIOClient client, T data, AckRequest ackSender) throws Exception {
            try {
                this.listener.onData(client, data, ackSender);
            } catch (Exception e) {
                handleException(ackSender, e);
            }
        }
    }

    public class MethodDataListener<T> implements DataListener<T> {

        private Method method;

        public MethodDataListener(Method method) {
            this.method = method;
        }

        @Override
        public void onData(SocketIOClient client, T data, AckRequest ackSender) throws Exception {
            Holder<AfterAckAction> afterAckActionHolder = new Holder<>();
            Object[] args = Arrays.stream(method.getParameterTypes()).map(type -> {
                if (type == SocketIOClient.class) {
                    return client;
                }
                if (type == AckRequest.class) {
                    return ackSender;
                }
                if (type == Client.class) {
                    return getClient(client);
                }
                if (type == AfterAckAction.class) {
                    if (afterAckActionHolder.value != null) {
                        throw new RuntimeException("只能有一个" + AfterAckAction.class.getSimpleName() + "参数");
                    }
                    afterAckActionHolder.value = new AfterAckAction();
                    return afterAckActionHolder.value;
                }
                return data;
            }).toArray(Object[]::new);

            try {
                Object result = method.invoke(BaseSocketService.this, args);

                if (responseAdvice != ResponseAdvice.IDENTITY) {
                    result = responseAdvice.beforeBodyWrite(result, method);
                    ackSender.sendAckData(result);
                } else if (method.getReturnType() != void.class) {
                    ackSender.sendAckData(result);
                }

                Optional.ofNullable(afterAckActionHolder.value)
                        .ifPresent(AfterAckAction::run);
            } catch (InvocationTargetException e) {
                Throwable target = e.getTargetException();
                if (target instanceof Exception) {
                    handleException(ackSender, (Exception) target);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void handleException(AckRequest ackSender, Exception e) throws Exception {
        ExceptionHandler<Exception> exceptionHandler = exceptionHandlers.stream()
                .filter(advice -> advice.supportException(e))
                .findFirst()
                .orElse(null);
        if (exceptionHandler != null) {
            Object result = exceptionHandler.handle(e);
            ackSender.sendAckData(result);
        } else {
            throw e;
        }
    }
}
