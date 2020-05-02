package org.ffpy.myspringboot.common.util;

import cn.hutool.core.lang.UUID;
import org.apache.commons.lang3.RandomStringUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 安全的的随机字符串工具类
 *
 * @author wenlongsheng
 */
public class RandomUtils {

    /** 安全随机数生成器，线程持有 */
    private static final ThreadLocal<SecureRandom> secureRandom = ThreadLocal.withInitial(() -> {
        try {
            return SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    });

    /**
     * 根据给定的字符数组生成指定长度的随机字符串，使用SecureRandom来生成
     *
     * @param length 字符串长度
     * @param chars  字符数组，生成的字符串会在其中随机选择
     * @return 随机字符串
     */
    public static String random(int length, char[] chars) {
        return RandomStringUtils.random(length, 0, 0, true, true, chars,
                secureRandom.get());
    }

    /**
     * 生成指定长度的字母+数字随机字符串，使用SecureRandom来生成
     *
     * @param length 字符串长度
     * @return 随机字符串
     */
    public static String randomAlphanumeric(int length) {
        return RandomStringUtils.random(length, 0, 0, true, true, null,
                secureRandom.get());
    }

    /**
     * 生成指定长度的字母随机字符串，使用SecureRandom来生成
     *
     * @param length 字符串长度
     * @return 随机字符串
     */
    public static String randomAlphabetic(int length) {
        return RandomStringUtils.random(length, 0, 0, true, false, null,
                secureRandom.get());
    }

    /**
     * 生成指定长度的数字随机字符串，使用SecureRandom来生成
     *
     * @param length 字符串长度
     * @return 随机字符串
     */
    public static String randomNumeric(int length) {
        return RandomStringUtils.random(length, 0, 0, false, true, null,
                secureRandom.get());
    }

    /**
     * 根据给定的字符数组生成指定长度的随机字符串，使用ThreadLocalRandom来生成
     *
     * @param length 字符串长度
     * @param chars  字符数组，生成的字符串会在其中随机选择
     * @return 随机字符串
     */
    public static String randomNotSecure(int length, char[] chars) {
        return RandomStringUtils.random(length, 0, 0, true, true, chars,
                ThreadLocalRandom.current());
    }

    /**
     * 生成指定长度的字母+数字随机字符串，使用ThreadLocalRandom来生成
     *
     * @param length 字符串长度
     * @return 随机字符串
     */
    public static String randomAlphanumericNotSecure(int length) {
        return RandomStringUtils.random(length, 0, 0, true, true, null,
                ThreadLocalRandom.current());
    }

    /**
     * 生成指定长度的字母随机字符串，使用ThreadLocalRandom来生成
     *
     * @param length 字符串长度
     * @return 随机字符串
     */
    public static String randomAlphabeticNotSecure(int length) {
        return RandomStringUtils.random(length, 0, 0, true, false, null,
                ThreadLocalRandom.current());
    }

    /**
     * 生成指定长度的数字随机字符串，使用ThreadLocalRandom来生成
     *
     * @param length 字符串长度
     * @return 随机字符串
     */
    public static String randomNumericNotSecure(int length) {
        return RandomStringUtils.random(length, 0, 0, false, true, null,
                ThreadLocalRandom.current());
    }

    /**
     * 获取SecureRandom，线程持有
     *
     * @return SecureRandom
     */
    public static SecureRandom getSecureRandom() {
        return secureRandom.get();
    }

    /**
     * 生成UUID
     *
     * @return UUID
     */
    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
