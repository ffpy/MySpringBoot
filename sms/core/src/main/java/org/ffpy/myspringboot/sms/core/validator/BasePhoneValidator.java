package org.ffpy.myspringboot.sms.core.validator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.ffpy.myspringboot.sms.core.config.SmsProperties;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

public abstract class BasePhoneValidator<A extends Annotation> implements ConstraintValidator<A, Object> {

    @Autowired
    protected SmsProperties smsProperties;

    protected boolean emptyAble;
    protected String phoneField;
    protected String countryCodeField;

    protected abstract boolean getEmptyAble(A constraintAnnotation);

    protected abstract String getPhoneField(A constraintAnnotation);

    protected abstract String getCountryCodeField(A constraintAnnotation);

    protected void init(A constraintAnnotation) {
    }

    protected abstract boolean doValid(Object obj, String phone, String countryCode, ConstraintValidatorContext context);

    /**
     * 通过重写{@link BasePhoneValidator#init(Annotation)}方法来实现相同的功能
     */
    @Override
    @Deprecated
    public void initialize(A constraintAnnotation) {
        emptyAble = getEmptyAble(constraintAnnotation);
        phoneField = getPhoneField(constraintAnnotation);
        countryCodeField = getCountryCodeField(constraintAnnotation);

        if (StringUtils.isEmpty(phoneField)) {
            throw new IllegalArgumentException("手机号字段名不能为空");
        }
        if (StringUtils.isEmpty(countryCodeField)) {
            throw new IllegalArgumentException("国家区号字段名不能为空");
        }

        init(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            String phone = BeanUtils.getSimpleProperty(obj, phoneField);
            String countryCode = null;
            if (StringUtils.isNotEmpty(countryCodeField)) {
                try {
                    countryCode = BeanUtils.getSimpleProperty(obj, countryCodeField);
                } catch (NoSuchMethodException ignored) {
                    // bean中没有国家区号字段，使用默认的国家区号
                    countryCode = smsProperties.getDefaultCountryCode();
                }
            }

            if (countryCode == null) {
                countryCode = "";
            }

            if (StringUtils.isBlank(phone)) {
                if (emptyAble) {
                    return true;
                } else {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("PHONE_CANNOT_BE_EMPTY")
                            .addConstraintViolation();
                    return false;
                }
            }

            return doValid(obj, phone, countryCode, context);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("读取字段值失败", e);
        }
    }
}
