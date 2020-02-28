package com.ganguomob.dev.myspringboot.socketio.advice;

/**
 * @author wenlongsheng
 * @date 2020/2/28
 */
public interface ExceptionHandler<E extends Exception> extends Advice {

    Object handle(E e);

    boolean supportException(Exception e);
}
