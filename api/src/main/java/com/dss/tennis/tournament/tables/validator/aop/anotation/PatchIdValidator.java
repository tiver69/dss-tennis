package com.dss.tennis.tournament.tables.validator.aop.anotation;


import com.dss.tennis.tournament.tables.validator.constraint.RequiredValidator;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RequiredValidator.class)
public @interface PatchIdValidator {
    String pathIdParameterName();
}
