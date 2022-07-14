package com.lk.ss2.validation.annotation;

import com.lk.ss2.validation.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Documented
@Target({FIELD, METHOD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
public @interface ValidEmail {

    String message() default "邮箱不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
