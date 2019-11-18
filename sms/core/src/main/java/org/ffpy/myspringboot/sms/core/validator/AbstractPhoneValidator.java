package org.ffpy.myspringboot.sms.core.validator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.ffpy.myspringboot.sms.core.config.SmsProperties;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

/**
 * 抽象的手机号校验注解校验器，用于继承
 *
 * @author wenlongsheng
 */
public abstract class AbstractPhoneValidator<A extends Annotation> implements ConstraintValidator<A, Object> {

    /** 是否允许手机号为空 */
    protected boolean emptyAble;

    /** 国家码字段名 */
    protected String countryCodeField;

    /** 手机号字段名 */
    protected String phoneField;

    @Autowired
    protected SmsProperties smsProperties;

    /**
     * 获取注解上的emptyAble值
     *
     * @param constraintAnnotation 校验注解
     * @return true允许为空，false反之
     */
    protected abstract boolean getEmptyAble(A constraintAnnotation);

    /**
     * 获取注解上的phoneField值
     *
     * @param constraintAnnotation 校验注解
     * @return phone字段名
     */
    protected abstract String getPhoneField(A constraintAnnotation);

    /**
     * 获取注解上的countryCodeField值
     *
     * @param constraintAnnotation 校验注解
     * @return countryCode字段名
     */
    protected abstract String getCountryCodeField(A constraintAnnotation);

    /**
     * 如有需要，通过重写此方法来执行初始化动作
     *
     * @param constraintAnnotation 校验注解
     */
    protected void init(A constraintAnnotation) {
    }

    /**
     * 校验手机号格式
     *
     * @param obj         要校验的对象
     * @param countryCode 国家区号
     * @param phone       手机号
     * @param context     ConstraintValidatorContext
     * @return true为校验通过，false为校验不通过
     */
    protected abstract boolean doValid(Object obj, String countryCode, String phone, ConstraintValidatorContext context);

    /**
     * 通过重写{@link AbstractPhoneValidator#init(Annotation)}方法来执行初始化动作，不要重写这个方法
     */
    @Override
    public final void initialize(A constraintAnnotation) {
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

            return doValid(obj, countryCode, phone, context);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("读取字段值失败", e);
        }
    }
}
