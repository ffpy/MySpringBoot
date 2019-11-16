package org.ffpy.myspringboot.sms.core.service.country;

import org.ffpy.myspringboot.sms.core.ui.response.CountryCodeResponse;

import java.util.List;

/**
 * 国家区号相关服务类接口
 *
 * @author wenlongsheng
 */
public interface CountryCodeService {

    /**
     * 获取支持的国家区号列表
     */
    List<CountryCodeResponse> getAllowedCountryCodes();

    /**
     * 判断是否是允许的国家区号
     *
     * @param countryCode 区号
     */
    boolean isCountryCodeAllowed(String countryCode);
}
