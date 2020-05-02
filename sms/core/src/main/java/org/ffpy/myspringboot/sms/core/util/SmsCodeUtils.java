package org.ffpy.myspringboot.sms.core.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 验证码工具类
 *
 * @author wenlongsheng
 */
public class SmsCodeUtils {

    /**
     * 判断验证码是否正确，忽略大小写
     *
     * @param excepted 实际的验证码
     * @param actual   用户输入的验证码
     * @return 验证通过返回true，否则返回false
     */
    public static boolean verifyCode(String excepted, String actual) {
        if (excepted == null || actual == null) {
            return false;
        }
        return StringUtils.equalsIgnoreCase(excepted, actual);
    }
}
