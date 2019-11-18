package org.ffpy.myspringboot.sms.core.constant;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * 手机号格式
 *
 * @author wenlongsheng
 */
public enum PhoneFormat {

    /** 未知国家的手机号 */
    UNKNOWN(() -> CountryCode.UNKNOWN, "\\d+"),

    /** 大陆手机号码 */
    CHINA(() -> CountryCode.CHINA, "((13[0-9])|(15[^4\\D])|(18[^14\\D])|(17[0-8])|(147))\\d{8}"),

    /** 香港手机号码 */
    HK(() -> CountryCode.HK, "[5689]\\d{7}"),

    /** 澳大利亚 */
    AUSTRALIA(() -> CountryCode.AUSTRALIA, "0?[23478]\\d{8}"),

    ;
    /** 对应的国家区号 */
    private final Supplier<CountryCode> countryCodeSupplier;

    /** 手机号匹配正则表达式 */
    private final String regex;

    /** 匹配器 */
    private Predicate<String> predicate;

    /** 加上国家区号的匹配器 */
    private Predicate<String> predicateWithCountryCode;

    PhoneFormat(Supplier<CountryCode> countryCodeSupplier, String regex) {
        Objects.requireNonNull(countryCodeSupplier, "国家码不能为null.");
        if (StringUtils.isEmpty(regex)) {
            throw new IllegalArgumentException("表达式不能为空");
        }

        if (regex.startsWith("^")) {
            throw new IllegalArgumentException("表达式不能以'^'开始");
        }
        if (regex.endsWith("$")) {
            throw new IllegalArgumentException("表达式不能以'$'结束");
        }

        this.countryCodeSupplier = countryCodeSupplier;
        this.regex = regex;
    }

    /**
     * 检验手机号格式是否正确
     *
     * @param phone 要校验的手机号
     * @return true为正确，false为不正确
     */
    public boolean valid(String phone) {
        if (predicate == null) {
            predicate = Pattern.compile("^" + regex + "$").asPredicate();
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
            predicateWithCountryCode = Pattern.compile(
                    "^(?:\\+?" + countryCodeSupplier.get().getCode() + ")?" + regex + "$")
                    .asPredicate();
        }
        return predicateWithCountryCode.test(phone);
    }
}
