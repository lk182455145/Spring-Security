package com.lk.ss2.validation;

import com.lk.ss2.dto.UserDto;
import com.lk.ss2.validation.annotation.ValidPasswordMatches;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<ValidPasswordMatches, UserDto> {

    @Override
    public void initialize(ValidPasswordMatches constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserDto value, ConstraintValidatorContext context) {
        return value.getPassword().equals(value.getMatchPassword());
    }
}
