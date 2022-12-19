package com.dss.tennis.tournament.tables.exception.handler;

import com.dss.tennis.tournament.tables.model.response.v1.ErrorResponse.ErrorData;
import org.springframework.stereotype.Service;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static com.dss.tennis.tournament.tables.exception.ErrorConstants.ErrorKey.GENERAL_BAD_REQUEST;

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
