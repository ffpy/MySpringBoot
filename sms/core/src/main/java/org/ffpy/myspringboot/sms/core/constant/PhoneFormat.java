package org.ffpy.myspringboot.sms.core.constant;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * 手机号格式
 */
public enum PhoneFormat {

    /** 未知国家的手机号 */
    UNKNOWN("", "\\d+"),

    /** 大陆手机号码11位数 */
    CHINA("86", "((13[0-9])|(15[^4\\D])|(18[^14\\D])|(17[0-8])|(147))\\d{8}"),

    /** 香港手机号码8位数，5|6|8|9开头+7位任意数 */
    HK("852", "[5689]\\d{7}"),

    /** 澳大利亚 */
    AUSTRALIA("61", "0?[23478]\\d{8}"),


    ;
    /** 国家区号 */
    private final String countryCode;

    /** 手机号匹配正则表达式 */
    private final String regex;

    /** 匹配器 */
    private Predicate<String> predicate;

    /** 加上国家区号的匹配器 */
    private Predicate<String> predicateWithCountryCode;

    /**
     * 根据国家区号获取对应的手机号格式
     *
     * @param countryCode 国家区号
     * @return 对应国家的手机号格式，如果找不到则返回{@link #UNKNOWN}
     */
    public static PhoneFormat of(String countryCode) {
        for (PhoneFormat phoneFormat : values()) {
            if (phoneFormat.countryCode.equals(countryCode)) {
                return phoneFormat;
            }
        }
        return UNKNOWN;
    }

    PhoneFormat(String countryCode, String regex) {
        if (StringUtils.isEmpty(countryCode)) {
            throw new IllegalArgumentException("countryCode cannot be empty.");
        }
        if (StringUtils.isEmpty(regex)) {
            throw new IllegalArgumentException("regex cannot be empty.");
        }

        // 加上首尾位置匹配
        if (regex.startsWith("^")) {
            throw new IllegalArgumentException("regex can't startsWith '^'");
        }
        if (regex.endsWith("$")) {
            throw new IllegalArgumentException("regex can't endsWith '$'");
        }

        this.countryCode = countryCode;
        this.regex = "^" + regex + "$";
    }

    /**
     * 检验手机号格式是否正确
     *
     * @param phone 要校验的手机号
     * @return true为正确，false为不正确
     */
    public boolean valid(String phone) {
        if (predicate == null) {
            predicate = Pattern.compile(regex).asPredicate();
        }
        return predicate.test(phone);
    }

    /**
     * 检验手机号格式是否正确，能够匹配带国家区号的号码
     *
     * @param phone 要校验的手机号
     * @return true为正确，false为不正确
     */
    public boolean validWithCountryCode(String phone) {
        if (predicateWithCountryCode == null) {
            predicateWithCountryCode = Pattern.compile("(?:\\+?" + countryCode + ")?" + regex).asPredicate();
        }
        return predicateWithCountryCode.test(phone);
    }
}
