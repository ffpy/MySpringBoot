package com.ganguomob.dev.myspringboot;

import lombok.Data;
import com.ganguomob.dev.myspringboot.sms.core.validator.SmsCodeValid;

@Data
@SmsCodeValid(SmsGroup.Names.LOGIN)
public class LoginRequest {

    private String countryCode;

    private String phone;

    private String code;
}
