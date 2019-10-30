package org.ffpy.myspringboot.sms.core.store.redis;

import org.ffpy.myspringboot.sms.core.store.SmsStore;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class SmsRedisStoreCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return context.getBeanFactory().getBean(SmsStore.class) == null;
    }
}
