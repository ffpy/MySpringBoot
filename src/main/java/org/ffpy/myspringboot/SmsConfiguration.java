package org.ffpy.myspringboot;

import org.ffpy.myspringboot.sms.aliyun.sender.AliyunSmsSender;
import org.ffpy.myspringboot.sms.core.SmsSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsConfiguration {

    @Bean
    public SmsSender smsSender() {
        return new AliyunSmsSender();
    }

//    @Bean
//    public SmsStore smsStore() {
//        return new SmsRedisStore();
//    }
}
