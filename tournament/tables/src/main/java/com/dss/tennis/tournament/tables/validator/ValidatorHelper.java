package com.dss.tennis.tournament.tables.validator;

import com.dss.tennis.tournament.tables.exception.DetailedException.DetailedErrorData;
import com.dss.tennis.tournament.tables.model.dto.AbstractSequentialDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ValidatorHelper<V> {

    @Autowired
    private Validator javaxValidator;

    public Set<DetailedErrorData> validateObject(V validationObject) {
        Set<String> errorMessages = javaxValidator.validate(validationObject).stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        return validationObject instanceof AbstractSequentialDTO ?
                collectErrorDetailsWithSequentialNumber(errorMessages,
                        ((AbstractSequentialDTO) validationObject)
                                .getSequenceNumber()) :
                collectErrorDetails(errorMessages);
    }

    private Set<DetailedErrorData> collectErrorDetailsWithSequentialNumber(Set<String> errorMessages,
                                                                           Integer sequenceNumber) {
        Set<DetailedErrorData> detailedErrorData = collectErrorDetails(errorMessages);
        detailedErrorData.forEach(detail -> detail.setSequentNumber(sequenceNumber));
        return detailedErrorData;
    }

    private Set<DetailedErrorData> collectErrorDetails(Set<String> errorMessages) {
        return errorMessages.stream().map(DetailedErrorData::new).collect(Collectors.toSet());
    }
}
