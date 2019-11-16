package org.ffpy.myspringboot.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

    private static final ThreadLocal<SecureRandom> secureRandom = ThreadLocal.withInitial(() -> {
        try {
            return SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    });

    public static String randomAlphanumeric(int count) {
        return RandomStringUtils.random(count, 0, 0, true, true, null,
                secureRandom.get());
    }

    public static String randomAlphabetic(int count) {
        return RandomStringUtils.random(count, 0, 0, true, false, null,
                secureRandom.get());
    }

    public static String randomNumeric(int count) {
        return RandomStringUtils.random(count, 0, 0, false, true, null,
                secureRandom.get());
    }

    public static String randomAlphanumericNotSecure(int count) {
        return RandomStringUtils.random(count, 0, 0, true, true, null,
                ThreadLocalRandom.current());
    }

    public static String randomAlphabeticNotSecure(int count) {
        return RandomStringUtils.random(count, 0, 0, true, false, null,
                ThreadLocalRandom.current());
    }

    public static String randomNumericNotSecure(int count) {
        return RandomStringUtils.random(count, 0, 0, false, true, null,
                ThreadLocalRandom.current());
    }
}
