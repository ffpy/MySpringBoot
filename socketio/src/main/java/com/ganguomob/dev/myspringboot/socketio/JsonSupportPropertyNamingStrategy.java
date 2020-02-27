package com.ganguomob.dev.myspringboot.socketio;

import com.corundumstudio.socketio.Configuration;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;

/**
 * 用于{@link com.corundumstudio.socketio.protocol.JsonSupport}的命名策略
 * 如果是自定义的数据类，使用指定的命名策略，否则使用驼峰法命名，否则会报错
 */
public class JsonSupportPropertyNamingStrategy extends PropertyNamingStrategy {

    private static final String[] IGNORE_PACKAGE_NAMES = {
            "java", Configuration.class.getPackage().getName()
    };

    private final PropertyNamingStrategy strategy;

    /**
     * @param strategy 指定的命名策略
     */
    public JsonSupportPropertyNamingStrategy(PropertyNamingStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public String nameForField(MapperConfig<?> config, AnnotatedField field, String defaultName) {
        return isCustomData(field) ?
                strategy.nameForField(config, field, defaultName) : defaultName;
    }

    @Override
    public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
        return isCustomData(method) ?
                strategy.nameForGetterMethod(config, method, defaultName) : defaultName;
    }

    @Override
    public String nameForSetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
        return isCustomData(method) ?
                strategy.nameForSetterMethod(config, method, defaultName) : defaultName;
    }

    @Override
    public String nameForConstructorParameter(MapperConfig<?> config, AnnotatedParameter ctorParam,
                                              String defaultName) {
        return isCustomData(ctorParam) ?
                strategy.nameForConstructorParameter(config, ctorParam, defaultName) : defaultName;
    }

    /**
     * 判断是不是自定义的数据类
     */
    private boolean isCustomData(AnnotatedMember member) {
        String className = member.getDeclaringClass().getName();
        for (String ignorePackageName : IGNORE_PACKAGE_NAMES) {
            if (className.startsWith(ignorePackageName)) {
                return false;
            }
        }
        return true;
    }
}
