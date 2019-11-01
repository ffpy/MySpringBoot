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
@Constraint(validatedBy = {SmsCodeValidValidatorImpl.class})
@Target({ElementType.TYPE, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface SmsCodeValid {

    /** 分组名 */
    String value();

    String message() default "SMS_CODE_NOT_VALID";

    /**
     * 手机号字段名
     */
    String phoneField() default "phone";

    /**
     * 国家区号字段名
     */
    String countryCodeField() default "countryCode";

    /**
     * 验证码字段名
     */
    String codeField() default "code";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
