package com.java6.nhom1.validation.constraints;

import com.java6.nhom1.validation.validator.PasswordConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordConstraintValidator.class)
public @interface PasswordConstraint {
    String message() default "Invalid Password";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
