package org.ffpy.myspringboot.sms.core.util;

public class PhoneFormatUtils {

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：13+任意数 15+除4的任意数 18+除1和4的任意数 17+除9的任意数 147
     */
    public static boolean isChinaPhone(String phone) {
        return phone.matches("((13[0-9])|(15[0-35-9])|(18[0235-9])|(17[0-8])|(147))\\d{8}");
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhone(String phone) {
        return phone.matches("[5689]\\d{7}");
    }
}
