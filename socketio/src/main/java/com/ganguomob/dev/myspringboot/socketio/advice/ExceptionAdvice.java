package com.ganguomob.dev.myspringboot.socketio.advice;

/**
 * @author wenlongsheng
 * @date 2020/2/28
 */
public interface ExceptionAdvice extends Advice {

    Object handle(Exception e);

    boolean supportException(Exception e);
}
