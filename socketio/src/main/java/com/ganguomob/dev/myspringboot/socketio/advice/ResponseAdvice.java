package com.ganguomob.dev.myspringboot.socketio.advice;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;

/**
 * @author wenlongsheng
 * @date 2020/2/28
 */
public interface ResponseAdvice extends Advice {

    ResponseAdvice IDENTITY = new ResponseAdvice() {
        @Override
        public boolean supports(SocketIOServer server, SocketIONamespace namespace) {
            return true;
        }

        @Override
        public Object beforeBodyWrite(Object body, Method handlerMethod) {
            return body;
        }
    };

    Object beforeBodyWrite(Object body, @Nullable Method handlerMethod);
}
