package org.ffpy.myspringboot.sms.core.validator;

import org.ffpy.myspringboot.sms.core.constant.PhoneFormat;

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
        return PhoneFormat.of(countryCode).valid(phone);
    }
}
