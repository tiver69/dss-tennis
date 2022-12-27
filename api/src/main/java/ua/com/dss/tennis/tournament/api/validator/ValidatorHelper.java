package ua.com.dss.tennis.tournament.api.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.dss.tennis.tournament.api.model.dto.AbstractSequentialDTO;
import ua.com.dss.tennis.tournament.api.model.dto.ErrorDataDTO;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ValidatorHelper<V> {

    @Autowired
    private Validator javaxValidator;

    public Set<ErrorDataDTO> validateObject(V validationObject) {
        return validateObject(validationObject, null);
    }

    public Set<ErrorDataDTO> validateObject(V validationObject, String customPointer) {
        Set<String> errorMessages = javaxValidator.validate(validationObject).stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        return validationObject instanceof AbstractSequentialDTO ?
                collectErrorDetailsWithSequentialNumber(errorMessages, customPointer,
                        ((AbstractSequentialDTO) validationObject).getSequenceNumber()) :
                collectErrorDetails(errorMessages, customPointer);
    }

    private Set<ErrorDataDTO> collectErrorDetailsWithSequentialNumber(Set<String> errorMessages, String customPointer,
                                                                      Byte sequenceNumber) {
        Set<ErrorDataDTO> detailedErrorData = collectErrorDetails(errorMessages, customPointer);
        detailedErrorData.forEach(detail -> detail.setSequentNumber(sequenceNumber));
        return detailedErrorData;
    }

    private Set<ErrorDataDTO> collectErrorDetails(Set<String> errorMessages, String customPointer) {
        return errorMessages.stream().map(ErrorDataDTO::new).peek(errorData -> errorData.setPointer(customPointer))
                .collect(Collectors.toSet());
    }
}
