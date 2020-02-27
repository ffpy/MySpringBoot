package com.ganguomob.dev.myspringboot.socketio.scan;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author wenlongsheng
 * @date 2020/2/21
 */
@Slf4j
public class SocketServiceScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    public static final String ATTR_BASE_PACKAGES = "basePackages";

    private Environment environment;
    private ResourceLoader resourceLoader;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                metadata.getAnnotationAttributes(SocketServiceScan.class.getName()));
        Objects.requireNonNull(attributes, "Attributes not found.");

        SocketServiceClassPathScanner scanner = getScanner(registry);
        scanner.setBeanNameGenerator(new SocketServiceBeanNameGenerator());
        scanner.doScan(getBasePackages((StandardAnnotationMetadata) metadata, attributes));
    }

    private SocketServiceClassPathScanner getScanner(BeanDefinitionRegistry registry) {
        SocketServiceClassPathScanner scanner = new SocketServiceClassPathScanner(registry);
        if (resourceLoader != null) {
            scanner.setResourceLoader(resourceLoader);
        }
        if (environment != null) {
            scanner.setEnvironment(environment);
        }
        return scanner;
    }

    private String[] getBasePackages(StandardAnnotationMetadata metadata, AnnotationAttributes attributes) {
        String[] basePackages = Arrays.stream(attributes.getStringArray(ATTR_BASE_PACKAGES))
                .filter(StringUtils::isNotEmpty)
                .toArray(String[]::new);
        if (basePackages.length == 0) {
            // 如果没有指定扫描路径，则默认扫描标记了SocketServiceScan注解的类所在的包
            basePackages = new String[]{
                    metadata.getIntrospectedClass().getPackage().getName()
            };
        }
        return basePackages;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
