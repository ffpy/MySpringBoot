package org.ffpy.myspringboot.common.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtils implements InitializingBean, ApplicationContextAware {

    private static Environment ENVIRONMENT;

    private static ApplicationContext applicationContext;

    @Autowired
    private Environment mEnvironment;

    public static Environment getEnvironment() {
        return ENVIRONMENT;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(Class<T> type) {
        return (T) applicationContext.getBean(type);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ENVIRONMENT = mEnvironment;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtils.applicationContext = applicationContext;
    }
}
