package com.dss.tennis.tournament.tables.validator.anotation.validator;

import com.dss.tennis.tournament.tables.validator.anotation.Required;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RequiredValidator implements ConstraintValidator<Required, String> {

    @Override
    public void initialize(Required annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext var2) {
        return StringUtils.isNotBlank(value);
    }
}
