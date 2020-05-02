package org.ffpy.myspringboot.socketio.scan;

import org.ffpy.myspringboot.socketio.service.SocketService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

import java.util.Optional;

/**
 * @author wenlongsheng
 * @date 2020/2/21
 */
@Slf4j
class SocketServiceBeanNameGenerator extends AnnotationBeanNameGenerator {

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        return Optional.ofNullable(definition.getBeanClassName())
                .map(className -> {
                    try {
                        return Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        log.error(e.getMessage(), e);
                    }
                    return null;
                })
                .map(cls -> cls.getAnnotation(SocketService.class))
                .map(SocketService::name)
                .filter(StringUtils::isNotEmpty)
                .orElseGet(() -> super.generateBeanName(definition, registry));
    }
}
