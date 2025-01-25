package com.klymenko.newmarketapi.dto.validators;

import com.klymenko.newmarketapi.enums.Roles;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class RoleValidator implements ConstraintValidator<ValidRole, String> {
    @Override
    public void initialize(ValidRole constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String role, ConstraintValidatorContext constraintValidatorContext) {
        if (role == null) {
            return false;
        }

        return Arrays.stream(Roles.values()).anyMatch(val -> val.name().equalsIgnoreCase(role));
    }
}
