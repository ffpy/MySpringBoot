package org.ffpy.myspringboot;

import lombok.Data;
import org.ffpy.myspringboot.sms.core.validator.SmsCodeValid;

@Data
@SmsCodeValid(SmsGroup.Names.LOGIN)
public class LoginRequest {

    private String countryCode;

    private String phone;

    private String code;
}
