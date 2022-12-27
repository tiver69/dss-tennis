package ua.com.dss.tennis.tournament.api.exception.handler;

import org.springframework.stereotype.Service;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ua.com.dss.tennis.tournament.api.model.definitions.ErrorResponse.ErrorData;

import static ua.com.dss.tennis.tournament.api.exception.ErrorConstants.ErrorKey.GENERAL_BAD_REQUEST;

@Service
public class TypeMismatchExceptionHandler extends SourceAwareExceptionHandler implements JacksonExceptionHandler {

    protected final String QUERY_PARAM_POINTER = "?";

    @Override
    public ErrorData createErrorData(Throwable exception) {
        MethodArgumentTypeMismatchException castedException = (MethodArgumentTypeMismatchException) exception;
        String pointer = QUERY_PARAM_POINTER + castedException.getName();

        return super.createErrorData(GENERAL_BAD_REQUEST, castedException.getValue().toString(), pointer);
    }
}
