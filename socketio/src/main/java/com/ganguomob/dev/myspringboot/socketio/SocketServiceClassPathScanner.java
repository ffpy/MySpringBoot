package com.ganguomob.dev.myspringboot.socketio;

import com.ganguomob.dev.myspringboot.socketio.anno.SocketService;
import com.ganguomob.dev.myspringboot.socketio.util.SocketServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author wenlongsheng
 * @date 2020/2/21
 */
@Slf4j
public class SocketServiceClassPathScanner extends ClassPathBeanDefinitionScanner {
    /** 扫描到的命名空间列表 */
    private static List<String> namespaces = new LinkedList<>();

    public static List<String> getNamespaces() {
        return namespaces;
    }

    public SocketServiceClassPathScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
        addIncludeFilter(new AnnotationTypeFilter(SocketService.class));
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> holderSet = super.doScan(basePackages);
        holderSet.forEach(holder -> {
            String className = holder.getBeanDefinition().getBeanClassName();
            try {
                Class<?> cls = Class.forName(className, false,
                        Thread.currentThread().getContextClassLoader());
                SocketService annotation = cls.getAnnotation(SocketService.class);
                namespaces.add(SocketServiceUtils.getNamespace(annotation.namespace()));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        });
        return holderSet;
    }
}
