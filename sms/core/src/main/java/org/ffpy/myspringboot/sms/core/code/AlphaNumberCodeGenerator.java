package org.ffpy.myspringboot.sms.core.code;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 字母+数字验证码生成器
 */
@Component
@ConditionalOnProperty(value = "sms.code.generator", havingValue = "alpha-number")
public class AlphaNumberCodeGenerator implements CodeGenerator {

    @Override
    public String generate(int length) {
        return RandomStringUtils.randomAlphanumeric(length).toUpperCase();
    }
}
