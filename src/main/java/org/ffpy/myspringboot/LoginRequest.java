package org.ffpy.myspringboot;


import lombok.Data;
import org.ffpy.myspringboot.sms.core.validator.SmsCode;
import org.ffpy.myspringboot.sms.core.validator.SmsCodeBean;

@Data
@SmsCode("login")
public class LoginRequest implements SmsCodeBean {

    private String countryCode;

    private String phone;

    private String code;
}
