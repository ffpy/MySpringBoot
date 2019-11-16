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
 * 国家区号校验注解
 *
 * @author wenlongsheng
 */
@Documented
@Constraint(validatedBy = {CountryCodeValidatorImpl.class})
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface CountryCodeValid {

    String message() default "COUNTRY_CODE_FORMAT_IS_INCORRECT";

    /**
     * 是否允许为空
     */
    boolean emptyAble() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
