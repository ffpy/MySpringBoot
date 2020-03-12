package com.ganguomob.dev.myspringboot.common.util;

import lombok.AllArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author wenlongsheng
 * @date 2020/3/12
 */
public class AnnotationUtils {

    /**
     * 获取元素上的注解的属性值
     *
     * @param element         元素
     * @param annotationClass 注解类型
     * @param checkDecorator  是否检查装饰器注解
     * @return 值
     */
    public static Attrs getValue(AnnotatedElement element, Class<? extends Annotation> annotationClass,
                                 boolean checkDecorator) {
        Annotation annotation = element.getDeclaredAnnotation(annotationClass);
        if (annotation != null) {
            return new DirectAttrs(annotation);
        }

        if (!checkDecorator) {
            return null;
        }

        for (Annotation anno : element.getDeclaredAnnotations()) {
            Annotation superAnno = anno.annotationType().getDeclaredAnnotation(annotationClass);
            if (superAnno != null) {
                return new DecoratorAttrs(anno, superAnno);
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T> T invoke(Object obj, String methodName) {
        try {
            Method method = obj.getClass().getMethod(methodName);
            return (T) method.invoke(obj);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public interface Attrs {

        <T> T get(String name);
    }

    @AllArgsConstructor
    public static class DirectAttrs implements Attrs {
        private final Annotation annotation;

        @Override
        public <T> T get(String name) {
            return invoke(annotation, name);
        }
    }

    @AllArgsConstructor
    public static class DecoratorAttrs implements Attrs {
        private final Annotation annotation;
        private final Annotation superAnnotation;

        @SuppressWarnings("unchecked")
        @Override
        public <T> T get(String name) {
            try {
                Method method = annotation.annotationType().getMethod(name);
                return (T) method.invoke(annotation);
            } catch (NoSuchMethodException e) {
                return invoke(superAnnotation, name);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
