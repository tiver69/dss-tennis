package com.dss.tennis.tournament.tables.validator.constraint;

import com.dss.tennis.tournament.tables.validator.constraint.anotation.Required;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RequiredValidator implements ConstraintValidator<Required, Object> {

    @Override
    public void initialize(Required annotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext var2) {
        if (value instanceof String) {
            return StringUtils.isNotBlank((String) value);
        }
        return value != null;
    }
}
