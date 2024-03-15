package com.bookappstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
public class DescriptionValidator implements ConstraintValidator<Description, String> {
    private static final int SIZE = 255;

    @Override
    public boolean isValid(String description, ConstraintValidatorContext context) {
        return description != null && description.length() <= SIZE;
    }
}
