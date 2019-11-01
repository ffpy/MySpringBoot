package org.ffpy.myspringboot.sms.core.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
