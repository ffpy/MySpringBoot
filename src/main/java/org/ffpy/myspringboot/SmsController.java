package org.ffpy.myspringboot;

import io.swagger.annotations.ApiOperation;
import org.ffpy.myspringboot.sms.core.exception.SendSmsFailException;
import org.ffpy.myspringboot.sms.core.service.country.CountryCodeService;
import org.ffpy.myspringboot.sms.core.service.sms.SmsService;
import org.ffpy.myspringboot.sms.core.ui.response.CountryCodeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sms")
@Validated
public class SmsController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private CountryCodeService countryCodeService;

    @GetMapping("/send")
    public String sendCode(@Validated SendCodeRequest request) throws SendSmsFailException {
        return smsService.sendCode(SmsGroup.LOGIN, request.getCountryCode(), request.getPhone());
    }

    @ApiOperation("获取支持的国家区号列表")
    @GetMapping("/allowed-country-code")
    public List<CountryCodeResponse> getAllowedCountryCodes() {
        return countryCodeService.getAllowedCountryCodes();
    }

    @PostMapping("/check")
    public String checkCode(@RequestBody @Validated LoginRequest request) {
        return "success: " + request;
    }
}
