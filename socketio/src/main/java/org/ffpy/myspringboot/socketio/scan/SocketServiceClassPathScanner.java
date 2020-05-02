package org.ffpy.myspringboot.socketio.scan;

import org.ffpy.myspringboot.socketio.service.SocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

/**
 * @author wenlongsheng
 * @date 2020/2/21
 */
@Slf4j
public class SocketServiceClassPathScanner extends ClassPathBeanDefinitionScanner {

    public SocketServiceClassPathScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
        addIncludeFilter(new AnnotationTypeFilter(SocketService.class));
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        return super.doScan(basePackages);
    }
}
