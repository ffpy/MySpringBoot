package org.ffpy.myspringboot.sms.core.validator;

import org.ffpy.myspringboot.sms.core.constant.CountryCode;
import org.ffpy.myspringboot.sms.core.util.PhoneFormatUtils;

import javax.validation.ConstraintValidatorContext;

class PhoneValidValidatorImpl extends BasePhoneValidator<PhoneValid> {

    @Override
    protected boolean getEmptyAble(PhoneValid constraintAnnotation) {
        return constraintAnnotation.emptyAble();
    }

    @Override
    protected String getPhoneField(PhoneValid constraintAnnotation) {
        return constraintAnnotation.phoneField();
    }

    @Override
    protected String getCountryCodeField(PhoneValid constraintAnnotation) {
        return constraintAnnotation.countryCodeField();
    }

    /**
     * 校验手机号格式是否正确
     */
    @Override
    protected boolean doValid(Object obj, String phone, String countryCode, ConstraintValidatorContext context) {
        switch (countryCode) {
            case CountryCode.COUNTRY_CODE_CHINA:
                return PhoneFormatUtils.isChinaPhone(phone);
            case CountryCode.COUNTRY_CODE_HK:
                return PhoneFormatUtils.isHKPhone(phone);
            default:
                return phone.matches("\\d+");
        }
    }
}
