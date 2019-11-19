package org.ffpy.myspringboot.sms.core.util;

/**
 * @author wenlongsheng
 */
public class PhoneNumberUtils {

    /**
     * 格式化国家区号
     */
    public static String normalCountryCode(String countryCode) {
        return countryCode.replaceAll("\\D", "");
    }

    /**
     * 格式化手机号
     */
    public static String normalPhone(String phone) {
        return phone.replaceAll("\\D", "");
    }
}
