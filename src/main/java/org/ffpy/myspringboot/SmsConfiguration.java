package org.ffpy.myspringboot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsConfiguration {

    /**
     * 注入短信分组类型
     */
    @Bean(name = "smsGroupClass")
    public Class<SmsGroup> smsGroup() {
        return SmsGroup.class;
    }
}
