package org.ffpy.myspringboot.sms.core.validator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.ffpy.myspringboot.sms.core.group.ISmsGroup;
import org.ffpy.myspringboot.sms.core.service.sms.SmsService;
import org.ffpy.myspringboot.sms.core.util.SmsCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;

/**
 * 短信验证码校验注解校验器
 */
public class SmsCodeValidValidatorImpl extends AbstractPhoneValidator<SmsCodeValid> {

    @Resource(name = "smsGroupClass")
    private Class<? extends ISmsGroup> smsGroupClass;

    @Autowired
    private SmsService smsService;

    private ISmsGroup group;

    private String codeField;

    @Override
    protected boolean getEmptyAble(SmsCodeValid constraintAnnotation) {
        return false;
    }

    @Override
    protected String getPhoneField(SmsCodeValid constraintAnnotation) {
        return constraintAnnotation.phoneField();
    }

    @Override
    protected String getCountryCodeField(SmsCodeValid constraintAnnotation) {
        return constraintAnnotation.countryCodeField();
    }

    @Override
    protected void init(SmsCodeValid constraintAnnotation) {
        group = ISmsGroup.ofName(constraintAnnotation.value(), smsGroupClass);
        codeField = constraintAnnotation.codeField();

        if (StringUtils.isEmpty(codeField)) {
            throw new IllegalArgumentException("验证码字段明不能为空");
        }
    }

    /**
     * 校验短信验证码是否正确
     */
    @Override
    protected boolean doValid(Object obj, String countryCode, String phone, ConstraintValidatorContext context) {
        try {
            String code = BeanUtils.getSimpleProperty(obj, codeField);

            if (StringUtils.isEmpty(code)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("CODE_CANNOT_BE_EMPTY")
                        .addConstraintViolation();
                return false;
            }

            String exceptedCode = smsService.getCode(group, countryCode, phone);
            boolean valid = SmsCodeUtils.verifyCode(exceptedCode, code);
            // 验证成功后移除验证码
            if (valid) {
                smsService.removeCode(group, countryCode, phone);
            }
            return valid;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("读取字段值失败", e);
        }
    }
}
