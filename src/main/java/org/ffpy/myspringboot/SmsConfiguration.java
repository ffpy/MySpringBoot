package org.ffpy.myspringboot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsConfiguration {

    @Bean(name = "smsGroupClass")
    public Class<SmsGroup> smsGroup() {
        return SmsGroup.class;
    }
}
