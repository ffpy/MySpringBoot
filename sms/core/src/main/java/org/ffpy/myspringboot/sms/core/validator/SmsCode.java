package org.ffpy.myspringboot.sms.core.validator;

import org.ffpy.myspringboot.sms.core.SmsGroup;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = {SmsCodeValidatorImpl.class})
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface SmsCode {

    SmsGroup value();

    String message() default "SMS_CODE_NOT_VALID";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
