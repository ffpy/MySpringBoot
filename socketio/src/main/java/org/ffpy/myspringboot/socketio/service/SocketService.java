package org.ffpy.myspringboot.socketio.service;

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

    /** 绑定的{@link com.corundumstudio.socketio.SocketIOServer}的bean名称，如果spring中只有一个实例，则可以为空 */
    String server() default "";

    /** 绑定的namespace */
    String namespace();
}
