package com.ganguomob.dev.myspringboot.sms.core.store;

import com.ganguomob.dev.myspringboot.sms.core.config.SmsProperties;
import org.apache.commons.lang3.StringUtils;
import com.ganguomob.dev.myspringboot.sms.core.group.ISmsGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 短信验证码Redis存储
 *
 * @author wenlongsheng
 */
public class SmsRedisStore implements SmsStore {

    /** 重复发送的键 */
    private static final String SMS_CODE_REPEAT_LIMIT = ":sms_code_repeat_limit:";

    /** 存储验证码的键 */
    private static final String SMS_CODE = ":sms_code:";

    /** 再次发送的值 */
    private static final String REPEAT_VALUE = "1";

    /** redis键的命名空间，即项目名称 */
    @Value("${sms.namespace}")
    private String namespace;

    @Autowired
    private SmsProperties smsProperties;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void saveCode(ISmsGroup group, String countryCode, String phone, String code) {
        String key = getKey(group, countryCode, phone);
        redisTemplate.opsForValue().set(key, code);
        redisTemplate.expire(key, smsProperties.getExpire(), TimeUnit.SECONDS);
    }

    @Override
    public String getCode(ISmsGroup group, String countryCode, String phone) {
        return redisTemplate.opsForValue().get(getKey(group, countryCode, phone));
    }

    @Override
    public void removeCode(ISmsGroup group, String countryCode, String phone) {
        redisTemplate.delete(getKey(group, countryCode, phone));
        redisTemplate.delete(getRepeatKey(group, countryCode, phone));
    }

    @Override
    public void addRepeatLimit(ISmsGroup group, String countryCode, String phone) {
        String repeatKey = getRepeatKey(group, countryCode, phone);
        redisTemplate.opsForValue().set(repeatKey, REPEAT_VALUE);
        redisTemplate.expire(repeatKey, smsProperties.getRepeatLimit(), TimeUnit.SECONDS);
    }

    @Override
    public boolean isInRepeatLimit(ISmsGroup group, String countryCode, String phone) {
        return Optional.ofNullable(redisTemplate.hasKey(getRepeatKey(group, countryCode, phone)))
                .orElse(false);
    }

    /**
     * 获取验证码存储的键
     *
     * @param group       短信分组
     * @param countryCode 国家区号
     * @param phone       手机号
     * @return 键
     */
    private String getKey(ISmsGroup group, String countryCode, String phone) {
        if (StringUtils.isBlank(group.getName())) {
            throw new IllegalArgumentException("group name cannot be blank");
        }
        if (StringUtils.isBlank(phone)) {
            throw new IllegalArgumentException("phone cannot be blank");
        }

        return namespace + SMS_CODE + group.getName() + ":" + countryCode + phone;
    }

    /**
     * 获取重复发送的键
     *
     * @param group       短信分组
     * @param countryCode 国家区号
     * @param phone       手机号
     * @return 键
     */
    private String getRepeatKey(ISmsGroup group, String countryCode, String phone) {
        if (StringUtils.isBlank(group.getName())) {
            throw new IllegalArgumentException("group name cannot be blank");
        }
        if (StringUtils.isBlank(phone)) {
            throw new IllegalArgumentException("phone cannot be blank");
        }

        return namespace + SMS_CODE_REPEAT_LIMIT + group.getName() + ":" + countryCode + phone;
    }
}
