package ua.com.dss.tennis.tournament.api.exception.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ua.com.dss.tennis.tournament.api.model.definitions.ErrorResponse.ErrorData;

import java.util.stream.Collectors;

import static ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorKey.GENERAL_BAD_REQUEST;

@Service
public class InvalidFormatExceptionHandler extends SourceAwareExceptionHandler implements JacksonExceptionHandler {

    protected final String EXCEPTION_OPEN_PATH = "[\"";
    protected final String EXCEPTION_CLOSE_PATH = "\"]";
    protected final String EXCEPTION_POINTER_PREFIX = "/";

    @Override
    public ErrorData createErrorData(Throwable exception) {
        InvalidFormatException castedException = (InvalidFormatException) exception;
        String parameter = castedException.getValue().toString();
        String pointer = castedException.getPath().stream().map(this::extractPathFromReference)
                .collect(Collectors.joining("/")).replaceAll("/\\[","[");
        pointer = StringUtils.isBlank(pointer) ? null : EXCEPTION_POINTER_PREFIX + pointer;

        return super.createErrorData(GENERAL_BAD_REQUEST, parameter, pointer);
    }

    private String extractPathFromReference(JsonMappingException.Reference reference) {
        if (StringUtils.isBlank(reference.getDescription())) return null;
        String refDescription = reference.getDescription();
        return refDescription.contains("java.util.ArrayList") ?
                StringUtils.substring(refDescription, refDescription.length() - 3) :
                StringUtils.substringBetween(refDescription, EXCEPTION_OPEN_PATH, EXCEPTION_CLOSE_PATH);
    }
}
