package org.ffpy.myspringboot.sms.core.code;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 字母验证码生成器
 */
@Component
@ConditionalOnProperty(value = "sms.code.generator", havingValue = "alpha")
public class AlphaCodeGenerator implements CodeGenerator {

    private static final char[] CHARS = "ABCDEFGHIJKLMNPOQRSTUVWXYZ".toCharArray();

    @Override
    public String generate(int length) {
        return RandomStringUtils.random(length, 0, 0,
                true, false, CHARS, ThreadLocalRandom.current());
    }
}
