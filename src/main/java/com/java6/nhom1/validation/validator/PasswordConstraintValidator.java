package com.java6.nhom1.validation.validator;

import com.java6.nhom1.validation.constraints.PasswordConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PasswordConstraintValidator implements ConstraintValidator<PasswordConstraint, String> {
    Pattern pwdPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");
    @Override
    public void initialize(PasswordConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) return false;
        if(value.length() <= 4) return false;
        return pwdPattern.matcher(value).matches();
    }
}
