package com.dss.tennis.tournament.tables.validator.anotation.validator;

import com.dss.tennis.tournament.tables.validator.constraint.RequiredValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class RequiredValidatorTest {

    private static final String VALID_LAST_NAME = "LastName  ";
    private static final String EMPTY_NAME = "    ";

    @Mock
    private ConstraintValidatorContext validatorContext;

    private final RequiredValidator testInstance = new RequiredValidator();

    @Test
    public void shouldPassValidation() {
        assertTrue(testInstance.isValid(VALID_LAST_NAME, validatorContext));
    }

    @Test
    public void shouldNotPassValidationForEmptyValue() {
        assertFalse(testInstance.isValid(EMPTY_NAME, validatorContext));
    }

    @Test
    public void shouldNotPassValidationForNullValue() {
        assertFalse(testInstance.isValid(null, validatorContext));
    }
}