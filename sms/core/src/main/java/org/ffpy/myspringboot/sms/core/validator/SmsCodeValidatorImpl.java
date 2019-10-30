package org.ffpy.myspringboot.sms.core.validator;

import org.apache.commons.lang3.StringUtils;
import org.ffpy.myspringboot.sms.core.SmsGroup;
import org.ffpy.myspringboot.sms.core.service.SmsService;
import org.ffpy.myspringboot.sms.core.util.SmsCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SmsCodeValidatorImpl implements ConstraintValidator<SmsCode, SmsCodeBean> {

    private SmsGroup group;

    @Autowired
    private SmsService smsService;

    @Override
    public void initialize(SmsCode constraintAnnotation) {
        group = constraintAnnotation.value();
    }

    /**
     * 校验短信验证码是否正确
     */
    @Override
    public boolean isValid(SmsCodeBean bean, ConstraintValidatorContext context) {
        if (bean == null) {
            return false;
        }

        if (StringUtils.isEmpty(bean.getPhone())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("PHONE_CANNOT_BE_BLANK")
                    .addConstraintViolation();
            return false;
        }

        if (StringUtils.isEmpty(bean.getCode())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("CODE_CANNOT_BE_EMPTY")
                    .addConstraintViolation();
            return false;
        }

        String code = smsService.getCode(group, bean.getCountryCode(), bean.getPhone());
        boolean valid = SmsCodeUtils.verifyCode(code, bean.getCode());
        // 验证成功后移除验证码
        if (valid) {
            smsService.removeCode(group, bean.getCountryCode(), bean.getPhone());
        }
        return valid;
    }
}
