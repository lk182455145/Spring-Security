package com.lk.ss2.validation.annotation;

import com.lk.ss2.validation.PasswordMatchesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Documented
@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
public @interface ValidPasswordMatches {

    String message() default "两次密码不一样";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
