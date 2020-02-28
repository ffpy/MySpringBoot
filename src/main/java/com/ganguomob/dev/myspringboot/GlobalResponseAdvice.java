package com.ganguomob.dev.myspringboot;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.ganguomob.dev.myspringboot.socketio.advice.ResponseAdvice;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author wenlongsheng
 * @date 2020/2/28
 */
@Component
public class GlobalResponseAdvice implements ResponseAdvice {

    @Override
    public Object beforeBodyWrite(Object body, @Nullable Method handlerMethod) {
        if (handlerMethod == null || handlerMethod.getReturnType() != Response.class) {
            body = new Response<>(0, body, "");
        }
        return body;
    }

    @Override
    public boolean supports(SocketIOServer server, SocketIONamespace namespace) {
        return true;
    }
}
