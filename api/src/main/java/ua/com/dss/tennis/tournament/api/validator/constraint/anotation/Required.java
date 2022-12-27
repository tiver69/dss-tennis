package ua.com.dss.tennis.tournament.api.validator.constraint.anotation;


import ua.com.dss.tennis.tournament.api.validator.constraint.RequiredValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RequiredValidator.class)
public @interface Required {
    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
