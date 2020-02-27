package com.ganguomob.dev.myspringboot.socketio.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注为SocketService，注入Spring，并且绑定指定的命名空间
 *
 * @author wenlongsheng
 * @date 2020/2/21
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SocketService {

    /** bean的名称，不指定则使用Spring的bean名称生成策略 */
    String name() default "";

    /** 命名空间 */
    String namespace();
}
