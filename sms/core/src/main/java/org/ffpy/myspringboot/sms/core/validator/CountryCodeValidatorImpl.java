package org.ffpy.myspringboot.sms.core.validator;

import org.apache.commons.lang3.StringUtils;
import org.ffpy.myspringboot.sms.core.service.country.CountryCodeService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CountryCodeValidatorImpl implements ConstraintValidator<CountryCodeValidator, String> {

    private boolean emptyAble;

    @Autowired
    private CountryCodeService countryCodeService;

    @Override
    public void initialize(CountryCodeValidator constraintAnnotation) {
        emptyAble = constraintAnnotation.emptyAble();
    }

    /**
     * 校验手机国家区号
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            if (emptyAble) {
                return true;
            } else {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("COUNTRY_CODE_CANNOT_BE_EMPTY")
                        .addConstraintViolation();
                return false;
            }
        }

        if (!value.matches("\\d{1,3}([- ]\\d{1,3})?")) {
            return false;
        }

        if (!countryCodeService.isCountryCodeAllowed(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("NOT_ALLOW_COUNTRY_CODE")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
