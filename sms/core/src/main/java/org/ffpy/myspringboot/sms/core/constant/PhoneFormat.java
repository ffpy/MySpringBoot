package org.ffpy.myspringboot.sms.core.constant;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * 手机号格式
 * 正则表达式参考: https://www.w3xue.com/exp/article/20197/48395.html
 *
 * @author wenlongsheng
 */
public enum PhoneFormat {

    /** 未知 */
    UNKNOWN(() -> CountryCode.UNKNOWN, "\\d+"),

    /** 中国大陆 */
    CHINA(() -> CountryCode.CHINA, "((13[0-9])|(15[^4\\D])|(18[^14\\D])|(17[0-8])|(147))\\d{8}"),

    /** 中国香港 */
    HONG_KONG(() -> CountryCode.HONG_KONG, "[5689]\\d{7}"),

    /** 中国台湾 */
    TAIWAN(() -> CountryCode.TAIWAN, "9\\d{8}"),

    /** 澳大利亚 */
    AUSTRALIA(() -> CountryCode.AUSTRALIA, "[23478]\\d{8}"),

    /** 阿尔及利亚 */
    ARABIC_ALGERIA(() -> CountryCode.ARABIC_ALGERIA, "(5|6|7)\\d{8}"),

    /** 叙利亚 */
    ARABIC_SYRIA(() -> CountryCode.ARABIC_SYRIA, "9\\d{8}"),

    /** 沙特阿拉伯 */
    ARABIC_SAUDI_ARABIA(() -> CountryCode.ARABIC_SAUDI_ARABIA, "5\\d{8}"),

    /** 美国 */
    AMERICA(() -> CountryCode.AMERICA, "[2-9]\\d{2}[2-9](?!11)\\d{6}"),

    /** 捷克 */
    CZECH(() -> CountryCode.CZECH, "[1-9][0-9]{2} ?[0-9]{3} ?[0-9]{3}"),

    /** 德国 */
    GERMANY(() -> CountryCode.GERMANY, "([\\(]{1}[0-9]{1,6}[\\)])?([0-9 \\.\\-\\/]{3,20})((x|ext|extension)[ ]?[0-9]{1,4})?"),

    /** 丹麦 */
    DENMARK(() -> CountryCode.DENMARK, "(\\d{8})"),

    /** 希腊 */
    GREECE(() -> CountryCode.GREECE, "(69\\d{8})"),

    /** 英国 */
    BRITAIN(() -> CountryCode.BRITAIN, "7\\d{9}"),

    /** 印度 */
    INDIA(() -> CountryCode.INDIA, "[789]\\d{9}"),

    /** 新西兰 */
    NEW_ZEALAND(() -> CountryCode.NEW_ZEALAND, "2\\d{7,9}"),

    /** 南非 */
    SOUTH_AFRICA(() -> CountryCode.SOUTH_AFRICA, "\\d{9}"),

    /** 西班牙 */
    SPAIN(() -> CountryCode.SPAIN, "(6\\d{1}|7[1234])\\d{7}"),

    /** 芬兰 */
    FINLAND(() -> CountryCode.FINLAND, "(4(0|1|2|4|5)?|50)\\s?(\\d\\s?){4,8}\\d"),

    /** 法国 */
    FRANCE(() -> CountryCode.FRANCE, "[67]\\d{8}"),

    /** 以色列 */
    ISRAEL(() -> CountryCode.ISRAEL, "([23489]|5[0248]|77)[1-9]\\d{6}"),

    /** 匈牙利 */
    HUNGARY(() -> CountryCode.HUNGARY, "(20|30|70)\\d{7}"),

    /** 意大利 */
    ITALY(() -> CountryCode.ITALY, "3\\d{2} ?\\d{6,7}"),

    /** 日本 */
    JAPAN(() -> CountryCode.JAPAN, "\\d{1,4}[ \\-]?\\d{1,4}[ \\-]?\\d{4}"),

    /** 马来西亚 */
    MALAYSIA(() -> CountryCode.MALAYSIA, "(([145]{1}(\\-|\\s)?\\d{7,8})|([236789]{1}(\\s|\\-)?\\d{7}))"),

    /** 挪威 */
    NORWAY(() -> CountryCode.NORWAY, "[49]\\d{7}"),

    /** 比利时 */
    BELGIUM(() -> CountryCode.BELGIUM, "4?\\d{8}"),

    /** 波兰 */
    POLAND(() -> CountryCode.POLAND, "[5-8]\\d ?\\d{3} ?\\d{2} ?\\d{2}"),

    /** 巴西 */
    BRAZIL(() -> CountryCode.BRAZIL, "[1-9]{2}\\-?[2-9]{1}\\d{3,4}\\-?\\d{4}"),

    /** 葡萄牙 */
    PORTUGAL(() -> CountryCode.PORTUGAL, "9[1236]\\d{7}"),

    /** 俄罗斯 */
    RUSSIA(() -> CountryCode.RUSSIA, "9\\d{9}"),

    /** 土耳其 */
    TURKEY(() -> CountryCode.TURKEY, "5\\d{9}"),

    /** 越南 */
    VIETNAM(() -> CountryCode.VIETNAM, "((1(2([0-9])|6([2-9])|88|99))|(9((?!5)[0-9])))([0-9]{7})"),

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

    /**
     * 获取对应的国家区号
     *
     * @return 对应的国家区号
     */
    public CountryCode getCountryCode() {
        return countryCodeSupplier.get();
    }
}
