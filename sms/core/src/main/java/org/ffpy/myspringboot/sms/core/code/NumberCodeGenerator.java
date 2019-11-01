package org.ffpy.myspringboot.sms.core.code;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 数字验证码生成器
 */
@Component
@ConditionalOnProperty(value = "sms.code.generator", havingValue = "number")
public class NumberCodeGenerator implements CodeGenerator {

    @Override
    public String generate(int length) {
        return RandomStringUtils.random(length, 0, 0,
                false, true, null, ThreadLocalRandom.current());
    }
}
