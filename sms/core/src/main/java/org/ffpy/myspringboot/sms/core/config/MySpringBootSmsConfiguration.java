package org.ffpy.myspringboot.sms.core.config;

import org.ffpy.myspringboot.sms.core.code.CodeGenerator;
import org.ffpy.myspringboot.sms.core.code.NumberCodeGenerator;
import org.ffpy.myspringboot.sms.core.store.SmsRedisStore;
import org.ffpy.myspringboot.sms.core.store.SmsStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 短信模块SpringBoot配置
 *
 * @author wenlongsheng
 */
@Configuration("_sms_configuration_")
public class MySpringBootSmsConfiguration {

    /**
     * 默认的验证码存储对象
     */
    @Bean
    @ConditionalOnMissingBean(SmsStore.class)
    public SmsStore smsStore() {
        return new SmsRedisStore();
    }

    /**
     * 默认的验证码生成器对象
     */
    @Bean
    @ConditionalOnMissingBean(CodeGenerator.class)
    public CodeGenerator smsCodeGenerator() {
        return new NumberCodeGenerator();
    }
}
