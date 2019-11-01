package org.ffpy.myspringboot;

import lombok.Data;
import org.ffpy.myspringboot.sms.core.validator.CountryCodeValidator;
import org.ffpy.myspringboot.sms.core.validator.PhoneValid;

@Data
@PhoneValid
public class SendCodeRequest {

    @CountryCodeValidator
    private String countryCode;

    private String phone;
}
