package org.ffpy.myspringboot.sms.core.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.ffpy.myspringboot.sms.core.SendSmsFailException;
import org.ffpy.myspringboot.sms.core.SmsGroup;
import org.ffpy.myspringboot.sms.core.config.SmsProperties;
import org.ffpy.myspringboot.sms.core.SmsSender;
import org.ffpy.myspringboot.sms.core.store.SmsStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsProperties smsProperties;

    @Autowired
    private SmsStore smsStore;

    @Autowired
    private SmsSender smsSender;

    @Override
    public String sendCode(SmsGroup group, String countryCode, String phone) throws SendSmsFailException {
        return sendCode(group, countryCode, phone, RandomStringUtils.randomNumeric(smsProperties.getLength()));
    }

    @Override
    public String sendCode(SmsGroup group, String countryCode, String phone, String code) throws SendSmsFailException {
        Objects.requireNonNull(group, "分组不能为null");
        Objects.requireNonNull(countryCode, "区号不能为null");
        if (StringUtils.isBlank(phone)) {
            throw new IllegalArgumentException("手机号不能为空");
        }
        if (StringUtils.isBlank(code)) {
            throw new IllegalArgumentException("验证码不能为空");
        }

        if (smsProperties.isDebug()) {
            code = smsProperties.getDebugCode();
        }

        if (smsStore.isInRepeatLimit(group, countryCode, phone)) {
            throw new IllegalArgumentException("SMS_REPEAT_SEND_NOT_ALLOW");
        }

        // debug模式不发送短信
        if (!smsProperties.isDebug()) {
            smsSender.send(countryCode, phone, group, code);
        }

        // 保存验证码并添加重复发送限制
        smsStore.saveCode(group, countryCode, phone, code);
        smsStore.addRepeatLimit(group, countryCode, phone);

        return code;
    }

    @Override
    public String getCode(SmsGroup group, String countryCode, String phone) {
        return smsStore.getCode(group, countryCode, phone);
    }

    @Override
    public void removeCode(SmsGroup group, String countryCode, String phone) {
        smsStore.removeCode(group, countryCode, phone);
    }
}
