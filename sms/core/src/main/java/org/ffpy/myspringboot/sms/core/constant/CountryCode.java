package org.ffpy.myspringboot.sms.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 国家区号
 *
 * @author wenlongsheng
 */
@AllArgsConstructor
public enum CountryCode {

    /** 未知 */
    UNKNOWN("", PhoneFormat.UNKNOWN),

    /** 中国大陆 */
    CHINA("86", PhoneFormat.CHINA),

    /** 中国香港 */
    HONG_KONG("852", PhoneFormat.HONG_KONG),

    /** 中国台湾 */
    TAIWAN("886", PhoneFormat.TAIWAN),

    /** 澳大利亚 */
    AUSTRALIA("61", PhoneFormat.AUSTRALIA),

    /** 阿尔及利亚 */
    ARABIC_ALGERIA("213", PhoneFormat.ARABIC_ALGERIA),

    /** 叙利亚 */
    ARABIC_SYRIA("963", PhoneFormat.ARABIC_SYRIA),

    /** 沙特阿拉伯 */
    ARABIC_SAUDI_ARABIA("966", PhoneFormat.ARABIC_SAUDI_ARABIA),

    /** 美国 */
    AMERICA("1", PhoneFormat.AMERICA),

    /** 捷克 */
    CZECH("420", PhoneFormat.CZECH),

    /** 德国 */
    GERMANY("49", PhoneFormat.GERMANY),

    /** 丹麦 */
    DENMARK("45", PhoneFormat.DENMARK),

    /** 希腊 */
    GREECE("30", PhoneFormat.GREECE),

    /** 英国 */
    BRITAIN("44", PhoneFormat.BRITAIN),

    /** 印度 */
    INDIA("91", PhoneFormat.INDIA),

    /** 新西兰 */
    NEW_ZEALAND("64", PhoneFormat.NEW_ZEALAND),

    /** 南非 */
    SOUTH_AFRICA("27", PhoneFormat.SOUTH_AFRICA),

    /** 西班牙 */
    SPAIN("34", PhoneFormat.SPAIN),

    /** 芬兰 */
    FINLAND("358", PhoneFormat.FINLAND),

    /** 法国 */
    FRANCE("33", PhoneFormat.FRANCE),

    /** 以色列 */
    ISRAEL("972", PhoneFormat.ISRAEL),

    /** 匈牙利 */
    HUNGARY("36", PhoneFormat.HUNGARY),

    /** 意大利 */
    ITALY("39", PhoneFormat.ITALY),

    /** 日本 */
    JAPAN("81", PhoneFormat.JAPAN),

    /** 马来西亚 */
    MALAYSIA("60", PhoneFormat.MALAYSIA),

    /** 挪威 */
    NORWAY("47", PhoneFormat.NORWAY),

    /** 比利时 */
    BELGIUM("32", PhoneFormat.BELGIUM),

    /** 波兰 */
    POLAND("48", PhoneFormat.POLAND),

    /** 巴西 */
    BRAZIL("55", PhoneFormat.BRAZIL),

    /** 葡萄牙 */
    PORTUGAL("351", PhoneFormat.PORTUGAL),

    /** 俄罗斯 */
    RUSSIA("7", PhoneFormat.RUSSIA),

    /** 土耳其 */
    TURKEY("90", PhoneFormat.TURKEY),

    /** 越南 */
    VIETNAM("84", PhoneFormat.VIETNAM),

    ;
    /** 国家区号哈希表 */
    private static Map<String, CountryCode> map;

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
        if (map == null) {
            map = Arrays.stream(values()).collect(Collectors.toMap(CountryCode::getCode, Function.identity()));
        }

        return map.getOrDefault(code, UNKNOWN);
    }
}
