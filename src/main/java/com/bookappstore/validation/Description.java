package com.bookappstore.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Constraint(validatedBy = IsbnValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface Description {
    String message() default "Invalid format of description";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
