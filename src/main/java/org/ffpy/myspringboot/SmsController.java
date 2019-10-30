package org.ffpy.myspringboot;

import org.ffpy.myspringboot.sms.core.SendSmsFailException;
import org.ffpy.myspringboot.sms.core.SmsGroup;
import org.ffpy.myspringboot.sms.core.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sms")
@Validated
public class SmsController {

    @Autowired
    private SmsService smsService;

    @GetMapping("/send")
    public String sendSms(String phone) throws SendSmsFailException {
        return smsService.sendCode(SmsGroup.LOGIN, phone);
    }
}
