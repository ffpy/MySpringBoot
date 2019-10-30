package org.ffpy.myspringboot.sms.core.store.redis;

import org.apache.commons.lang3.StringUtils;
import org.ffpy.myspringboot.sms.core.SmsGroup;
import org.ffpy.myspringboot.sms.core.config.SmsProperties;
import org.ffpy.myspringboot.sms.core.store.SmsStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 短信验证码存储
 */
@Repository
@Conditional(SmsRedisStoreCondition.class)
public class SmsRedisStore implements SmsStore {

    private static final String SMS_CODE_REPEAT_LIMIT = ":sms_code_repeat_limit:";
    private static final String SMS_CODE = ":sms_code:";

    /** redis键的命名空间，即项目名称 */
    @Value("${sms.namespace}")
    private String namespace;

    @Autowired
    private SmsProperties smsProperties;

    /** 再次发送的值 */
    private static final String REPEAT_VALUE = "1";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void saveCode(SmsGroup group, String countryCode, String phone, String code) {
        String key = getKey(group, countryCode, phone);
        redisTemplate.opsForValue().set(key, code);
        redisTemplate.expire(key, smsProperties.getExpire(), TimeUnit.SECONDS);
    }

    @Override
    public String getCode(SmsGroup group, String countryCode, String phone) {
        return redisTemplate.opsForValue().get(getKey(group, countryCode, phone));
    }

    @Override
    public void removeCode(SmsGroup group, String countryCode, String phone) {
        redisTemplate.delete(getKey(group, countryCode, phone));
        redisTemplate.delete(getRepeatKey(group, countryCode, phone));
    }

    @Override
    public void addRepeatLimit(SmsGroup group, String countryCode, String phone) {
        String repeatKey = getRepeatKey(group, countryCode, phone);
        redisTemplate.opsForValue().set(repeatKey, REPEAT_VALUE);
        redisTemplate.expire(repeatKey, smsProperties.getRepeatLimit(), TimeUnit.SECONDS);
    }

    @Override
    public boolean isInRepeatLimit(SmsGroup group, String countryCode, String phone) {
        return Optional.ofNullable(redisTemplate.hasKey(getRepeatKey(group, countryCode, phone)))
                .orElse(false);
    }

    private String getKey(SmsGroup group, String countryCode, String phone) {
        if (StringUtils.isBlank(group.getName())) {
            throw new IllegalArgumentException("group name cannot be blank");
        }
        if (StringUtils.isBlank(phone)) {
            throw new IllegalArgumentException("phone cannot be blank");
        }

        return namespace + SMS_CODE + group.getName() + ":" + countryCode + phone;
    }

    private String getRepeatKey(SmsGroup group, String countryCode, String phone) {
        if (StringUtils.isBlank(group.getName())) {
            throw new IllegalArgumentException("group name cannot be blank");
        }
        if (StringUtils.isBlank(phone)) {
            throw new IllegalArgumentException("phone cannot be blank");
        }

        return namespace + SMS_CODE_REPEAT_LIMIT + group.getName() + ":" + countryCode + phone;
    }
}
