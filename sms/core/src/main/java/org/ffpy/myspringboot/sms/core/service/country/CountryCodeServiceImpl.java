package org.ffpy.myspringboot.sms.core.service.country;

import cn.hutool.core.collection.CollectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.ffpy.myspringboot.sms.core.config.SmsProperties;
import org.ffpy.myspringboot.sms.core.ui.response.CountryCodeResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 国家区号相关服务类
 *
 * @author wenlongsheng
 */
@Service
public class CountryCodeServiceImpl implements InitializingBean, CountryCodeService {

    /** 支持的区号列表 */
    private List<CountryCodeResponse> countryCodeResponses;

    /** 允许的国家区号 */
    private Set<String> allowCountryCodeSet;

    @Autowired
    private SmsProperties smsProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (CollectionUtil.isEmpty(smsProperties.getCountryCodes())) {
            countryCodeResponses = Collections.emptyList();
            allowCountryCodeSet = null;
        } else {
            countryCodeResponses = smsProperties.getCountryCodes().stream()
                    .map(String::trim)
                    .map(it -> {
                        String[] split = StringUtils.split(it, '_');
                        if (split.length == 2) {
                            String code = split[0].trim();
                            String country = split[1].trim();

                            if (!code.matches("\\d+")) {
                                throw new IllegalArgumentException("区号必须为纯数字");
                            }
                            if (StringUtils.isEmpty(country)) {
                                throw new IllegalArgumentException("国家名称不能为空");
                            }

                            return new CountryCodeResponse(code, country);
                        } else {
                            throw new IllegalArgumentException("格式不正确: " + it + "，正确的格式为: [国家名称]_[国家区号]，不带[]");
                        }
                    })
                    .collect(Collectors.toList());

            allowCountryCodeSet = countryCodeResponses.stream()
                    .map(CountryCodeResponse::getCode)
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public List<CountryCodeResponse> getAllowedCountryCodes() {
        return countryCodeResponses;
    }

    @Override
    public boolean isCountryCodeAllowed(String countryCode) {
        return allowCountryCodeSet == null || allowCountryCodeSet.contains(countryCode);
    }
}
