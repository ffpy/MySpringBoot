package org.ffpy.myspringboot.sms.core.validator;

import org.apache.commons.lang3.StringUtils;
import org.ffpy.myspringboot.sms.core.service.country.CountryCodeService;
import org.ffpy.myspringboot.sms.core.util.PhoneNumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 国家区号校验注解检验器
 */
public class CountryCodeValidatorImpl implements ConstraintValidator<CountryCodeValid, String> {

    /** 是否运行国家区号为空 */
    private boolean emptyAble;

    @Autowired
    private CountryCodeService countryCodeService;

    @Override
    public void initialize(CountryCodeValid constraintAnnotation) {
        emptyAble = constraintAnnotation.emptyAble();
    }

    /**
     * 校验手机国家区号
     */
    @Override
    public boolean isValid(String countryCode, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(countryCode)) {
            if (emptyAble) {
                return true;
            } else {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("COUNTRY_CODE_CANNOT_BE_EMPTY")
                        .addConstraintViolation();
                return false;
            }
        }

        countryCode = PhoneNumberUtils.normalCountryCode(countryCode);

        if (!countryCode.matches("\\d{1,3}([- ]\\d{1,3})?")) {
            return false;
        }

        if (!countryCodeService.isCountryCodeAllowed(countryCode)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("NOT_ALLOW_COUNTRY_CODE")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
