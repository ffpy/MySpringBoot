package org.ffpy.myspringboot.sms.core.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author wenlongsheng
 */
public class CountryCodeUtils {

    /**
     * 去掉加号
     */
    public static String normalCountryCode(String countryCode) {
        if (StringUtils.startsWith(countryCode, "+")) {
            return countryCode.substring(1);
        }
        return countryCode;
    }
}
