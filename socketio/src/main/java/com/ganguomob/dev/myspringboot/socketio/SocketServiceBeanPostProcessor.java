package com.ganguomob.dev.myspringboot.socketio;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.ganguomob.dev.myspringboot.socketio.anno.EventHandler;
import com.ganguomob.dev.myspringboot.socketio.anno.SocketService;
import com.ganguomob.dev.myspringboot.socketio.util.SocketServiceUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author wenlongsheng
 * @date 2020/2/21
 */
@Component
public class SocketServiceBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    private SocketIOServer server;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof BaseSocketService) {
            BaseSocketService socketService = (BaseSocketService) bean;
            Class<? extends BaseSocketService> cls = socketService.getClass();
            SocketService annotation = cls.getAnnotation(SocketService.class);

            // 注入Namespace到SocketService
            String namespace = SocketServiceUtils.getNamespace(annotation.namespace());
            if (StringUtils.isEmpty(namespace)) {
                throw new RuntimeException(cls.getName() + "的namespace不能为空");
            }
            SocketIONamespace socketNamespace = server.getNamespace(namespace);
            if (socketNamespace == null) {
                throw new RuntimeException("找不到namespace: " + namespace);
            }
            socketService.setNamespace(socketNamespace);

            bindEventListeners(cls, socketService, socketNamespace);
        }
        return bean;
    }

    /**
     * 绑定Event事件到Service的方法
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void bindEventListeners(Class<? extends BaseSocketService> cls,
                                    BaseSocketService service, SocketIONamespace namespace) {
        for (Method method : cls.getMethods()) {
            EventHandler eventHandler = method.getAnnotation(EventHandler.class);
            if (eventHandler == null) {
                continue;
            }

            String event = eventHandler.value();
            if (StringUtils.isEmpty(event)) {
                throw new RuntimeException("方法" + method.getName() + "的event名称不能为空");
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
                        })
                        .toArray(Object[]::new);

                Object result = method.invoke(service, args);

                if (method.getReturnType() != void.class) {
                    ackSender.sendAckData(result);
                }
            });
        }
    }
}
