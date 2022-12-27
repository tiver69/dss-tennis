package ua.com.dss.tennis.tournament.api.validator.aop.anotation;


import ua.com.dss.tennis.tournament.api.validator.constraint.RequiredValidator;

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
