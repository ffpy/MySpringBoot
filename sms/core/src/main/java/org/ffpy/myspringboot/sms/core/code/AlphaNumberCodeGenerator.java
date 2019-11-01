package org.ffpy.myspringboot.sms.core.code;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 字母+数字验证码生成器
 */
@Component
@ConditionalOnProperty(value = "sms.code.generator", havingValue = "alpha-number")
public class AlphaNumberCodeGenerator implements CodeGenerator {

    // 去掉容易混淆的字符
    private static final char[] CHARS = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray();

    @Override
    public String generate(int length) {
        return RandomStringUtils.random(length, 0, 0,
                true, true, CHARS, ThreadLocalRandom.current());
    }
}
