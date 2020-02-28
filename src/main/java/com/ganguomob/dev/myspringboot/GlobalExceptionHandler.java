package com.ganguomob.dev.myspringboot;

import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.ganguomob.dev.myspringboot.socketio.advice.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author wenlongsheng
 * @date 2020/2/28
 */
@Component
public class GlobalExceptionHandler implements ExceptionHandler {

    @Override
    public Object handle(Exception e) {
        return new Response<>(HttpStatus.BAD_REQUEST.value(), null, e.getMessage());
    }

    @Override
    public boolean supportException(Exception e) {
        return e instanceof CommonException;
    }

    @Override
    public boolean supports(SocketIOServer server, SocketIONamespace namespace) {
        return true;
    }
}
