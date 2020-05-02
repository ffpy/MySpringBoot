package org.ffpy.myspringboot.socketio.scan;

import org.ffpy.myspringboot.socketio.service.SocketService;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启{@link SocketService}注解自动扫描
 * Github地址: https://github.com/ffpy/MySpringBoot/tree/ganguo/socketio
 *
 * @author wenlongsheng
 * @date 2020/2/21
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SocketServiceScannerRegistrar.class)
public @interface SocketServiceScan {

    @AliasFor("basePackages")
    String[] value() default {};

    /** 扫描的包，不指定则扫描标记了{@link SocketServiceScan}注解的类所在的包 */
    @AliasFor("value")
    String[] basePackages() default {};
}
