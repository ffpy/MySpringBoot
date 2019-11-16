package org.ffpy.myspringboot.sms.core.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 手机号格式校验注解，用在类上，校验指定字段上的手机号的格式是否正确，
 * 如果类不存在countryCode字段，则会使用sms.countryCode.default属性的值来作为默认国家区号来校验
 *
 * @author wenlongsheng
 */
@Documented
@Constraint(validatedBy = {PhoneValidValidatorImpl.class})
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface PhoneValid {

    String message() default "PHONE_FORMAT_IS_INCORRECT";

    /**
     * 是否允许为空
     */
    boolean emptyAble() default false;

    /**
     * 手机号字段名
     */
    String phoneField() default "phone";

    /**
     * 国家区号字段名
     */
    String countryCodeField() default "countryCode";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
