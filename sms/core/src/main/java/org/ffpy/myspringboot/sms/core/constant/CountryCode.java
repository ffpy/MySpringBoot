package org.ffpy.myspringboot.sms.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * 国家区号
 *
 * @author wenlongsheng
 */
@AllArgsConstructor
public enum CountryCode {

    /** 未知 */
    UNKNOWN("", PhoneFormat.UNKNOWN),

    /** 大陆国家区号 */
    CHINA("86", PhoneFormat.CHINA),

    /** 香港国家区号 */
    HK("852", PhoneFormat.HK),

    /** 澳大利亚 */
    AUSTRALIA("61", PhoneFormat.AUSTRALIA),

    ;
    /** 国家区号 */
    @Getter
    @NonNull
    private final String code;

    /** 对应的手机号格式 */
    @Getter
    private final PhoneFormat phoneFormat;

    /**
     * 根据国家区号获取对应的枚举类
     *
     * @param code 国家区号
     * @return 对应的枚举类，找不到则返回{@link #UNKNOWN}
     */
    public static CountryCode of(String code) {
        for (CountryCode countryCode : values()) {
            if (countryCode.code.equals(code)) {
                return countryCode;
            }
        }
        return UNKNOWN;
    }
}
