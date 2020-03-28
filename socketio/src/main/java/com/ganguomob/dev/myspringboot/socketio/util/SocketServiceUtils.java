package com.ganguomob.dev.myspringboot.socketio.util;

import com.corundumstudio.socketio.protocol.JacksonJsonSupport;
import com.corundumstudio.socketio.protocol.JsonSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.ganguomob.dev.myspringboot.socketio.scan.JsonSupportPropertyNamingStrategy;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

/**
 * @author wenlongsheng
 * @date 2020/2/21
 */
public class SocketServiceUtils {

    /**
     * 给命名空间加上'/'前缀
     */
    public static String getNamespace(String namespace) {
        return namespace.startsWith("/") ? namespace : "/" + namespace;
    }

    /**
     * 包装自定义的ObjectMapper为JsonSupport
     */
    public static JsonSupport createJsonSupport(ObjectMapper objectMapper) {
        return createJsonSupport(objectMapper, null);
    }

    /**
     * 包装自定义的ObjectMapper为JsonSupport
     */
    public static JsonSupport createJsonSupport(ObjectMapper objectMapper, Consumer<ObjectMapper> extraAction) {
        // 通过反射来使用自定义的ObjectMapper
        objectMapper = objectMapper.copy();
        objectMapper.setPropertyNamingStrategy(
                new JsonSupportPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE));

        JacksonJsonSupport jsonSupport = new JacksonJsonSupport();
        Class<? extends JacksonJsonSupport> cls = JacksonJsonSupport.class;

        Field field;
        try {
            field = cls.getDeclaredField("objectMapper");
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("filed objectMapper not found.", e);
        }

        Method init;
        try {
            init = cls.getDeclaredMethod("init", ObjectMapper.class);
            init.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("method init not found.", e);
        }

        try {
            field.set(jsonSupport, objectMapper);
            init.invoke(jsonSupport, objectMapper);

            if (extraAction != null) {
                extraAction.accept(objectMapper);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return jsonSupport;
    }
}
