package org.ffpy.myspringboot.sms.core.config;

import org.ffpy.myspringboot.sms.core.store.SmsStore;
import org.ffpy.myspringboot.sms.core.store.redis.SmsRedisStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("_sms_configuration_")
public class MySpringBootSmsConfiguration {

    @Bean
    @ConditionalOnMissingBean(SmsStore.class)
    public SmsStore smsStore() {
        return new SmsRedisStore();
    }
}
