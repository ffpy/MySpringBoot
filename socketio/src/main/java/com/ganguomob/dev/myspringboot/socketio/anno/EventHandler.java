package com.ganguomob.dev.myspringboot.socketio.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用在SocketService的方法上，用于绑定Event到指定的方法。
 * 如果参数类型是{@link com.corundumstudio.socketio.SocketIOClient}或者
 * {@link com.corundumstudio.socketio.AckRequest}，会注入对应的对象，否则会注入客户端传过来的数据，
 *
 * 注意只能有一个数据参数，否则会报错。
 * 如果有返回数据，则直接用return返回即可。
 * 方法的访问修饰符应该为public，否则不生效。
 *
 * @author wenlongsheng
 * @date 2020/2/26
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventHandler {

    /** 绑定的Event名称，如果不指定则默认为方法名称 */
    String value() default "";
}
