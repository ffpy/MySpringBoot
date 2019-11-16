package org.ffpy.myspringboot.sms.core.validator;

import org.apache.commons.lang3.StringUtils;
import org.ffpy.myspringboot.sms.core.service.country.CountryCodeService;
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

        // 去掉加号
        if (value.startsWith("+")) {
            value = value.substring(1);
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
