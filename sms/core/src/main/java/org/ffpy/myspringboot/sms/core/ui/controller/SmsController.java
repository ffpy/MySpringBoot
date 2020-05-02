package org.ffpy.myspringboot.sms.core.ui.controller;

import org.ffpy.myspringboot.sms.core.config.SmsProperties;
import org.ffpy.myspringboot.sms.core.service.country.CountryCodeService;
import org.ffpy.myspringboot.sms.core.ui.response.GroupResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.ffpy.myspringboot.sms.core.exception.SendSmsFailException;
import org.ffpy.myspringboot.sms.core.group.ISmsGroup;
import org.ffpy.myspringboot.sms.core.service.sms.SmsService;
import org.ffpy.myspringboot.sms.core.ui.request.SendCodeRequest;
import org.ffpy.myspringboot.sms.core.ui.response.CountryCodeResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Github地址: https://github.com/ffpy/MySpringBoot/tree/ganguo/sms
 *
 * @author wenlongsheng
 */
@Api(tags = "05. 短信")
@RestController
@RequestMapping("/api/supplier/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private CountryCodeService countryCodeService;

    @Resource(name = "smsGroupClass")
    private Class<? extends ISmsGroup> smsGroupClass;

    @Autowired
    private SmsProperties smsProperties;

    @ApiOperation("发送短信验证码")
    @PostMapping("/send")
    public String sendCode(@RequestBody @Validated SendCodeRequest request) throws SendSmsFailException {
        if (StringUtils.isEmpty(request.getCountryCode())) {
            request.setCountryCode(smsProperties.getDefaultCountryCode());
        }
        return smsService.sendCode(ISmsGroup.ofName(request.getGroup(), smsGroupClass),
                request.getCountryCode(), request.getPhone());
    }

    @ApiOperation("获取支持的短信分组列表")
    @GetMapping("/groups")
    public List<GroupResponse> getGroups() {
        return Arrays.stream(smsGroupClass.getEnumConstants())
                .map(smsGroup -> new GroupResponse(smsGroup.getName(), smsGroup.getDesc()))
                .collect(Collectors.toList());
    }

    @ApiOperation("获取支持的国家区号列表")
    @GetMapping("/allowed-country-code")
    public List<CountryCodeResponse> getAllowedCountryCodes() {
        return countryCodeService.getAllowedCountryCodes();
    }
}
