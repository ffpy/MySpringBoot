package org.ffpy.myspringboot.sms.core.config;

import org.ffpy.myspringboot.sms.core.code.CodeGenerator;
import org.ffpy.myspringboot.sms.core.code.NumberCodeGenerator;
import org.ffpy.myspringboot.sms.core.store.SmsStore;
import org.ffpy.myspringboot.sms.core.store.SmsRedisStore;
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

    @Bean
    @ConditionalOnMissingBean(CodeGenerator.class)
    public CodeGenerator smsCodeGenerator() {
        return new NumberCodeGenerator();
    }
}
